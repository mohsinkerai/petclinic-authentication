import csv
import xlrd
import zipfile
import sys
import os

# main
param_1= sys.argv[1] # input file location
param_2= sys.argv[2] # zip extraction path

xlsmFilePath = 'data.xlsm'
csvFilePath = 'data.csv'
sheetName = 'sheet2'

zip = zipfile.ZipFile(r''+param_1)
zip.extractall(r'' + param_2)

workbook = xlrd.open_workbook (param_2 + '/' + data.xlsm)

os.remove(param_2 + '/' + csvFilePath)      #removing existing csv file from output folder

for sheet in workbook.sheets():
    if sheet.name == sheetName:
        with open((param_2+'/'+csvFilePath).format(sheet.name), 'wb') as f:
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

os.remove(param_2 +'/' + xlsmFilePath)  #removing input xlsm file from output folder