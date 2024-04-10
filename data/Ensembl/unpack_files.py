import gzip
import shutil
from glob import glob
from tqdm.auto import tqdm

DATA_PATH = r'D:\MA\data\Ensembl\\'

for file in tqdm(glob(DATA_PATH + '**\*tsv.gz'), desc='Extracting files'):
    # save at top level without .gz postfix
    out_path = DATA_PATH + file[file.rindex('\\') + 1:-3]
    with gzip.open(file, 'rb') as f_in:
        with open(out_path, 'wb') as f_out:
            shutil.copyfileobj(f_in, f_out)
