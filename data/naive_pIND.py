"""
Basic script to calculate pINDs. This script is not optimized in any way.
"""
import pandas as pd
from glob import glob
from tqdm.auto import tqdm

FOLDER = 'uefa'
ENDING = 'csv'
SEP = ','
HEADER = 0 # 0 or None
THRESHOLD = 0.743
NULL_VALUES = {'', 'EMPTY', 'NONE', 'NULL'}
NULL_IS_SUBSET = True # Null == Null and Null is a subset of everything
NULL_NOT_SUBSET = False # Null != Null

### Unary pINDs ###
attributes = {}
for raw_table_path in glob(FOLDER + '/*.' + ENDING):
    table = pd.read_csv(raw_table_path, sep=SEP, header=HEADER, dtype=str)
    table_path = raw_table_path[len(FOLDER)+1:]
    attributes[table_path] = []
    for ind, column in enumerate(table.columns):
        attributes[table_path].append({'name': f'{column}', 'id': ind, 'values' : list(table[column])})

candidates = []
for table in attributes:
    for dependend in attributes[table]:
        for ref_table in attributes:
            for referenced in attributes[ref_table]:
                if ref_table == table and dependend['id'] == referenced['id']:
                    continue
                candidates.append({'dep': {'table': table, 'columns': [dependend['id']]}, 'ref': {'table': ref_table, 'columns': [referenced['id']]}})        

unary_pINDs = {}
nary_size = 1
out = {}
while len(candidates) > 0:
    pINDs = {}
    count = 0

    for candidate in tqdm(candidates, desc=f'validating {nary_size}-nary pINDs'):
        dependend_table = candidate['dep']['table']
        dependend_cols = candidate['dep']['columns']
        dep_size = len(attributes[dependend_table][dependend_cols[0]]['values'])

        dep = [[attributes[dependend_table][c]['values'][i] for c in dependend_cols] for i in range(dep_size)]

        referenced_table = candidate['ref']['table']
        referenced_cols = candidate['ref']['columns']
        ref_size = len(attributes[referenced_table][referenced_cols[0]]['values'])

        ref = [[attributes[referenced_table][c]['values'][i] for c in referenced_cols] for i in range(ref_size)]

        violations_left = int((1-THRESHOLD) * dep_size)

        is_pIND = True
        for dep_values in dep:
            matches = False
            for ref_values in ref:
                matches = True
                for i in range(nary_size):
                    if NULL_IS_SUBSET and dep_values[i] in NULL_VALUES:
                        continue
                    if dep_values[i] != ref_values[i]:
                        matches = False
                        break
                if matches:
                    break
            if not matches:
                violations_left -= 1
                if violations_left < 0:
                    is_pIND = False
                    break
        
        if is_pIND:
            count += 1
            if dependend_table not in pINDs:
                pINDs[dependend_table] = {}
            if str(dependend_cols) not in pINDs[dependend_table]:
                pINDs[dependend_table][str(dependend_cols)] = []
            pINDs[dependend_table][str(dependend_cols)].append(candidate['ref'])
            if nary_size == 1:
                col_index = dependend_cols[0]
                if dependend_table not in unary_pINDs:
                    unary_pINDs[dependend_table] = {}
                if col_index not in unary_pINDs[dependend_table]:
                    unary_pINDs[dependend_table][col_index] = {}
                if referenced_table not in unary_pINDs[dependend_table][col_index]:
                    unary_pINDs[dependend_table][col_index][referenced_table] = []
                unary_pINDs[dependend_table][col_index][referenced_table].append(referenced_cols[0])
         
    print(f'{nary_size}-nary pINDs: {count}')
    
    for dependend_table in pINDs:
        for dependend_cols in pINDs[dependend_table]:
            for ref in pINDs[dependend_table][dependend_cols]:
                ref_string = '(' + ','.join([ref['table'] + '.' + attributes[ref['table']][i]['name'] for i in ref['columns']]) + ') > '
                if ref_string not in out:
                    out[ref_string] = []
                out[ref_string].append('('+','.join([dependend_table + '.' + attributes[dependend_table][i]['name'] for i in [int(k) for k in dependend_cols[1:-1].split(',')]])+')')

    nary_size += 1
    ### generate next canditate set ###
    candidates = []
    for dependend_table in pINDs:
        for dependend_cols in pINDs[dependend_table]:
            dep_cols = [int(i) for i in dependend_cols[1:-1].split(',')] # load indices
            for dep_candidate in unary_pINDs[dependend_table]:
                # Permutation escape
                if dep_candidate <= max(dep_cols):
                    continue
                for references in pINDs[dependend_table][dependend_cols]:
                    if references['table'] not in unary_pINDs[dependend_table][dep_candidate]:
                        continue
                    for ref_candidate in unary_pINDs[dependend_table][dep_candidate][references['table']]:
                        if ref_candidate in references['columns']:
                            continue
                        dep_col_candidates = dep_cols + [dep_candidate]
                        ref_col_candidates = references['columns'] + [ref_candidate]
                        if references['table'] == dependend_table:
                            if len(set(ref_col_candidates).intersection(set(dep_col_candidates))) > 0:
                                continue
                        candidates.append({'dep': {'table': dependend_table, 'columns': dep_col_candidates},
                                            'ref': {'table': references['table'], 'columns': ref_col_candidates}})

