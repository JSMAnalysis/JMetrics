#!/usr/bin/env python3

'''
    Python script to generate an horizontal histogram of the stability from a csv file.
    usage : python stability_histogram.py <csv_file_name>
'''

import numpy as np
import matplotlib.pyplot as plt
import sys


def instability_histogram():
    csv = np.genfromtxt(sys.argv[1], delimiter=';', dtype=None, names=True, encoding=None, usecols=("Granule", "I"))

    csv = sorted(csv, key=lambda x: x[1])

    labels = []
    datax = []
    datay = []

    for i in range(len(csv)):
        labels.append(csv[i][0])
        datax.append(i)
        datay.append(csv[i][1])

    plt.figure(figsize=(20, 0.5 * len(datax)))
    plt.axes([0.3,0.1,0.6,0.8])
    plt.barh(datax, datay, align='center')
    plt.yticks(datax, labels)
    plt.xlabel("Instability")
    plt.title("Instability by granule")
    plt.show()


if __name__ == "__main__":
    instability_histogram()


