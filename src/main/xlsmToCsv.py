import csv
import xlrd
import zipfile
import sys
import os

# main
param_1= sys.argv[1] #input file location
param_2= sys.argv[2] #input xlsm filename
param_3= sys.argv[3] #input xlsm sheet name
param_4= sys.argv[4] #output file location
param_5= sys.argv[5] #output csv filename

zip = zipfile.ZipFile(r''+param_1)
zip.extractall(r''+param_4)

workbook = xlrd.open_workbook(param_4+'/'+param_2)

os.remove(param_4+'/'+param_5)      #removing existing csv file from output folder

for sheet in workbook.sheets():
    if sheet.name == param_3:
        with open((param_4+'/'+param_5).format(sheet.name), 'wb') as f:
            writer = csv.writer(f)
            emptyCol = []
            for row in range(sheet.nrows):
                out = []
                counter = -1
                for cell in sheet.row_values(row):
                    counter += 1
                    if(row == 0):
                        if(cell == ""):
                            emptyCol.append(counter)
                            continue
                    else:
                        if counter in emptyCol:
                            continue
                    try:
                        out.append(cell.encode('utf8'))
                    except:
                        out.append(cell)
                writer.writerow(out)

os.remove(param_4+'/'+param_2)  #removing input xlsm file from output folder