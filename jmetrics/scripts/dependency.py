#!/usr/bin/env python3

'''
    Defines classes and methods helping with handling dependency scripts.
'''


from retrieve_csv_data import retrieve_data


def setup_dependencies_data(file):
    # Arity is currently unused (and will probably remain)
    csv = retrieve_data(file, ("GranuleSrc", "GranuleDst", "Type", "Arity"))
    dependencies = []
    for i in range(len(csv)):
        dependencies.append(Dependency(csv[i][0], csv[i][1], csv[i][2], csv[i][3]))
    granulesSrc = set([d.src for d in dependencies])
    granulesDst = set([d.dst for d in dependencies])
    granuleNames = granulesSrc.union(granulesDst)
    granules = []
    for g in granuleNames:
        efferentDependencies = list(filter(lambda d: d.src == g, dependencies))
        afferentDependencies = list(filter(lambda d: d.dst == g, dependencies))
        granules.append(Granule(g, afferentDependencies, efferentDependencies))
    return granules, dependencies


class Granule:
    def __init__(self, name, afferentDependencies, efferentDependencies):
        self.name = name
        self.afferentDependencies = afferentDependencies
        self.efferentDependencies = efferentDependencies
    def getCaArity(self):
        return len([d for d in self.afferentDependencies])
    def getCeArity(self):
        return len([d for d in self.efferentDependencies])
    def getCaBarInfo(self):
        return Granule.getBarInfo(self.afferentDependencies)
    def getCeBarInfo(self):
        return Granule.getBarInfo(self.efferentDependencies)
    def getBarInfo(dependencies):
        return [
            len([d for d in dependencies if d.type == "Inheritance"]),
            len([d for d in dependencies if d.type == "Association"]),
            len([d for d in dependencies if d.type == "UseLink"])
        ]


class Dependency:
    '''Reprensents a dependency between 2 granules.'''
    def __init__(self, src, dst, type, arity):
        self.src = src
        self.dst = dst
        self.type = type
        self.arity = arity
    DependencyType = {
        "Inheritance":  "red",
        "Association":  "blue",
        "UseLink":      "green"
    }
