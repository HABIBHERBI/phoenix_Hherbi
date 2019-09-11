# phenix-challenge Habib HERBI


## Pré-requis
* _Java_ en version 8
* _Scala_ en version 2.12.8 
* _Sbt_ en version 1.2.8


## Utilisation

Avec JAR:
```
java -jar votrejar.jar -i chemin/dossier/donnees/entrantes -o chemin/dossier/donnees/produite
```


Le dossier en entrée par defaut est le dossier data de l'énoncé,
Si vous ne mentionnez pas le dossier en sortie, tout sera créé dans le dossier en cours sous le répertoire `phenix-output``.


### Commandes sbt


Compilation:
```
sbt compile test:compile
```

Lancement des tests:
```
sbt test
```

Lancement de l'application:
```
sbt run -i input/folder -o output/folder
```

Packaging:
```
sbt assembly
```
