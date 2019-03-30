import numpy as np


def retrieve_data(file, usecols):
    return np.genfromtxt(file, delimiter=';', dtype=None, encoding=None, names=True, usecols=usecols)
