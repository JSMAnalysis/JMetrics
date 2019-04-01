#!/usr/bin/env python3

'''
    Python script that computes statistical values on the metrics
'''
USAGE = "python statistics.py <csv_file>"


from retrieve_csv_data import retrieve_data
import numpy as np
import sys


def setup_data():
    csv = retrieve_data(sys.argv[1], ("Ce", "Ca", "A", "I", "Dn"))
    return csv


def print_means(ce, ca, a, i, dn):
    print("Mean values per metric component :")
    print("\tCe : " + str(np.mean(ce)))
    print("\tCa : " + str(np.mean(ca)))
    print("\tA : " + str(np.mean(a)))
    print("\tI : " + str(np.mean(i)))
    print("\tDn : " + str(np.mean(dn)))


def print_median(ce, ca, a, i, dn):
    print("Median values per metric component :")
    print("\tCe : " + str(np.median(ce)))
    print("\tCa : " + str(np.median(ca)))
    print("\tA : " + str(np.median(a)))
    print("\tI : " + str(np.median(i)))
    print("\tDn : " + str(np.median(dn)))


def print_variance(ce, ca, a, i, dn):
    print("Variance values per metric component :")
    print("\tCe : " + str(np.var(ce)))
    print("\tCa : " + str(np.var(ca)))
    print("\tA : " + str(np.var(a)))
    print("\tI : " + str(np.var(i)))
    print("\tDn : " + str(np.var(dn)))


def print_std_deviation(ce, ca, a, i, dn):
    print("Standard deviation values per metric component :")
    print("\tCe : " + str(np.std(ce)))
    print("\tCa : " + str(np.std(ca)))
    print("\tA : " + str(np.std(a)))
    print("\tI : " + str(np.std(i)))
    print("\tDn : " + str(np.std(dn)))


def compute_statistics():
    data = setup_data()
    ce = []
    ca = []
    a = []
    i = []
    dn = []
    for metrics in data:
        ce.append(metrics[0])
        ca.append(metrics[1])
        a.append(metrics[2])
        i.append(metrics[3])
        dn.append(metrics[4])
    print_means(ce, ca, a, i, dn)
    print_variance(ce, ca, a, i, dn)
    print_std_deviation(ce, ca, a, i, dn)
    print_median(ce, ca, a, i, dn)


if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Bad CLI args (Require martinMetrics csv file).\nUsage : " + USAGE)
        exit(1)
    compute_statistics()