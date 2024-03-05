from glob import glob
import json

DATA_PATH = r"M:\MA\data\musicbrainz"

key_dict = {
    'area': {
        'keys': ['name', 'type',  'iso-3166-1-codes', 'iso-3166-2-codes', 'iso-3166-3-codes', ['life-span', 'begin'], ['life-span', 'end'], 'disambiguation', 'id'],
        'header': '"name","type","iso-3166-1-codes","iso-3166-2-codes","iso-3166-3-codes","begin","end","disambiguation","mbid"'
    },
    'artist': {
        'keys': ['name','sort-name',['area', 'id'],'gender','ipis','isnis','type',['life-span', 'begin'], ['life-span', 'end'],'disambiguation','id'],
        'header': '"name","sort_name","area","gender","IPI","ISNI","type","begin","end","disambiguation","mbid"'
    }
}
NULL = ''
c = 0
for file in glob(DATA_PATH + '/*'):
    name = file[len(DATA_PATH)+1:]
    if name not in key_dict:
        continue
    print(f'starting')
    d = key_dict[name]['keys']
    with open(file + '.csv', 'w', encoding='utf-8') as output:
        output.write(key_dict[name]['header'])
        output.write('\n')
        with open(file, 'r', encoding='utf-8') as raw_input:
            line = raw_input.readline()
            while line:
                values = []
                data = json.loads(line)
                # print(f"{data}")
                for key in d:
                    if type(key) == list:
                        values.append(f'"{NULL if (key[0] not in data or data[key[0]] == None) else data[key[0]].get(key[1], NULL)}"')
                    else:
                        values.append(f'"{data.get(key, NULL)}"')
                output.write(",".join(values))
                output.write('\n')
                line = raw_input.readline()
                # break