from glob import glob

for file in glob('*.tbl'):
    print(file)
    with open(file, 'r') as original:
        with open('corrected/' + file, 'w') as copy:
            line = original.readline()
            while line:
                copy.write(line[:-2] + '\n')
                line = original.readline()