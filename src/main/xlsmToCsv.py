import csv
import xlrd
import zipfile
import sys
import os

# main
param_1= sys.argv[1] # zip file
param_2= sys.argv[2] # extraction path

xlsmFilePath = 'data.xlsm'
csvFilePath = 'data.csv'
sheetName = 'sheet2'
print(param_1)
zip = zipfile.ZipFile(r''+param_1)
zip.extractall(r'' + param_2)

workbook = xlrd.open_workbook (param_2 + '\\' + xlsmFilePath)
print(workbook)
# os.remove(param_2 + '\\' + csvFilePath)      #removing existing csv file from output folder

for sheet in workbook.sheets():
    if sheet.name == sheetName:
        with open((param_2+'/'+csvFilePath).format(sheet.name), 'w',  encoding='utf8', newline='') as f:
            writer = csv.writer(f)
            emptyCol = []
            for row in range(sheet.nrows):
                out = []
                counter = -1
                for cell in sheet.row_values(row):
                    print(cell)
                    counter += 1
                    if(row == 0):
                        if(cell == ""):
                            emptyCol.append(counter)
                            continue
                    else:
                        if counter in emptyCol:
                            continue
                    if(type(cell) == 'str'):
                        newStr = cell.replace(",", "")
                    else:
                        newStr = cell
                    try:
                        out.append(newStr)
                    except:
                        out.append(newStr)
                print(type(out))
                writer.writerow(out)

# os.remove(param_2 +'/' + xlsm)  #removing input xlsm file from output folder