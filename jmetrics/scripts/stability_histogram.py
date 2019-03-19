#!/usr/bin/env python3

'''
    Python script to generate horizontal histogram of the stability from a csv file.
    usage : python stability_histogram.py <csv_file>
'''

from retrieve_csv_data import retrieve_data
import matplotlib.pyplot as plt
import sys



def setupData():
    csv = retrieve_data(sys.argv[1], ("Granule", "I"))
    csv = sorted(csv, key=lambda x: x[1])
    labels = []
    datax = []
    datay = []
    for i in range(len(csv)):
        labels.append(csv[i][0])
        datax.append(i)
        datay.append(csv[i][1])
    return labels, datax, datay

def instability_histogram():
    labels, datax, datay = setupData()
    plt.figure(figsize=(20, 0.5 * len(datax)))
    plt.axes([0.3, 0.1, 0.6, 0.8])
    plt.barh(datax, datay, align='center')
    plt.yticks(datax, labels)
    plt.xlabel("Instability")
    plt.title("Instability by granule")
    plt.show()



if __name__ == "__main__":
    instability_histogram()
