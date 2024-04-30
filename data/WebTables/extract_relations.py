import json
import pandas as pd
import numpy as np

with open('D:\MA\data\WebTables\sample', 'r', encoding='ANSI') as f:
    for ind, line in enumerate(f.readlines()):
        data = json.loads(line)
        relation = np.array(data['relation'])
        df = pd.DataFrame(data=relation[:,1:].T, columns=relation[:,0])

        df.to_csv(f'D:\MA\data\WebTables\{ind}.csv', index=False)   