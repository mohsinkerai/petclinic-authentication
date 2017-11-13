import csv
import xlrd
import zipfile

zip = zipfile.ZipFile(r'D:\input.zip')
zip.extractall(r'D:\output')

workbook = xlrd.open_workbook('D://output/input.xlsm')
for sheet in workbook.sheets():
    if sheet.name == "Data":
        with open('D://output.csv'.format(sheet.name), 'wb') as f:
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
