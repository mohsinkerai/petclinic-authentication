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
sheetName = 'Sheet1'

zip = zipfile.ZipFile(r''+param_1)
zip.extractall(r'' + param_2)

xlsmFilePath = 'data.xlsm'
csvFilePath = 'data.csv'
sheetName = 'Sheet1'
