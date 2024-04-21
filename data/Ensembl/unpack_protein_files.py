import gzip
import shutil
from glob import glob
from tqdm.auto import tqdm

DATA_PATH = r"M:\MA\data\ensembl-compara\homologies\\"

for file in tqdm(glob(DATA_PATH + '**\Compara.111.protein_default.homologies.tsv.gz'), desc='Extracting files'):
    # save at top level without .gz postfix
    out_path = r"M:\MA\data\ensembl-protein\\" + file[file.rindex('homologies\\') + 11:-46] + '.csv'
    with gzip.open(file, 'rb') as f_in:
        with open(out_path, 'wb') as f_out:
            shutil.copyfileobj(f_in, f_out)
