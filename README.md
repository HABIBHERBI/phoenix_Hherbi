# phenix-challenge Habib HERBI


## Pré-requis
* _Java_ en version 8
* _Scala_ en version 2.12.8 
* _Sbt_ en version 1.2.8


## Utilisation

Avec JAR:

use :
```

sbt assembly 

java -jar given_name.jar -i chemin/dossier/donnees/entrantes -o chemin/dossier/donnees/produite
```

already built jar: Phoenix_Habib_H-master\target\scala-2.12\phenix-challenge_Habib-assembly-0.1.0-SNAPSHOT.jar



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
