# Visualisateur trafic réseau


Ce projet a pour but de creer un visualisateur de traffic avec une trame sous un fichier en format txt.
Ce fichier doit pouvoir comprendre la liste des protocoles suivant:
    - Couche 2: Ethernet
    - Couche 3: IPv4
    - Couche 4: TCP
    - Couche 7: HTTP

Les fichiers code se situe dans le dossier src
Les fichiers binaire dans le dossier bin
Les fichiers de test contenant differentes traces se situe dans le dossier trace 

Dans le dossier src, il y a 4 dossiers:

    -Le dossier traitement qui permet de lire un fichier au format txt et de le transformer en une liste de liste de String.
    Chaque sous liste contient une trame decomposé en une liste de 2 octets

    -Le dossier exception contenant les classes java permettant de gerer les exceptions de plusieurs classe java du projet.
    Contient FormatInvalidException , OctetInvalidException , ProtocoleInvalidException
    
    -Le dossier protocole qui contient toute les manipulations à faire pour avoir les trames decomposer en protocole.
    Contient les fichiers Protocole, IPV4 , Ethernet ,HTTP,TCP , Encapsulation ,MultiTrame
    
    -Le dossier viewer qui permet d'afficher le visualisateur dans une fenetre dans une fentre graphique .La fenetre graphique à été realisé ave WindowBuilder.
    Contient les fichiers Fenetre , FenetreInit , TableMode , logoSU
    
    
Afiichage graphique du visualisateur de trame

![image](https://user-images.githubusercontent.com/79419364/211219084-14cd6803-0021-4825-ac8f-cd3d0e828d54.png)
