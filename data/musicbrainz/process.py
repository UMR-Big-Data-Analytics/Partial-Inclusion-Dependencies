from glob import glob
import json

DATA_PATH = r"M:\MA\data\musicbrainz"

key_dict = {
    'area': {
        'keys': ['id', 'name', 'type', ['life-span', 'begin'], ['life-span', 'end'], 'iso-3166-1-codes', 'iso-3166-2-codes', 'iso-3166-3-codes'],
        'header': '"mbid","name","type","begin","end","iso-3166-1-codes","iso-3166-2-codes","iso-3166-3-codes"'
    }
}

key_set = set()
c = 0
for file in glob(DATA_PATH + '/*'):
    name = file[len(DATA_PATH)+1:]
    if name not in key_dict:
        continue
    print(name)
    d = key_dict[name]['keys']
    with open(name + '.csv', 'w', encoding='utf-8') as output:
        with open(file, 'r', encoding='utf-8') as raw_input:
            line = raw_input.readline()
            while line != None:
                values = []
                data = json.loads(line)
                for key in d:
                    if type(key) == list:
                        values.append(None if key[0] not in data else data[key[0]][key[1]])
                

                line = raw_input.readline()
    break
print(key_set)