import json
from glob import glob
import pandas as pd
import numpy as np

for file in glob('./raw/*.json'):
    with open(file, 'r', encoding='ANSI') as f:
        data = json.load(f)
    
    print(file)
    new_name = file[file.rindex('\\') + 1:-5] + '.csv'
    relation = np.array(data['relation'])
    df = pd.DataFrame(data=relation[:,1:].T, columns=relation[:,0])

    df.to_csv(new_name, index=False)   