package fpdu;

import java.io.Serializable;
import java.util.HashMap;

public class PI extends PIBase implements Serializable {
	
	
	/*
	 * PeSIT doc
	 * 
	 * 
1 Utilisation d'un CRC
2 Diagnostic
3 Identification Demandeur
4 Identification Serveur
5 Contrôle d'accès
6 Numéro de version
7 Option points de synchronisation
11 Type du fichier
12 Nom du fichier
13 Identificateur du transfert
14 Attributs demandés
15 Transfert relancé
16 Code-données
17 Priorité de transfert
18 Point de relance
19 Code fin de transfert
20 Numéro du point de synchronisation
21 Compression
22 Type d'accès
23 Resynchronisation
25 Taille maximale d'une entité de données
26 Temporisation de surveillance
27 Nombre d'octets de données
28 Nombre d'articles
29 Compléments de diagnostic
31 Format d'article
32 Longueur d'article
33 Organisation du fichier
34 Prise en compte de la signature
36 Sceau SIT
37 Label du fichier
38 Longueur de la clé
39 Déplacement de la clé dans l'enregistrement
41 Unité de réservation d'espace
42 Valeur maximale de réservation d'espace
51 Date et heure de création
52 Date et heure de dernière extraction
61 Identificateur Client
62 Identificateur Banque
63 Contrôle d'accès fichier
64 Date et heure du serveur
71 Type d'authentification
72 Eléments d'authentification
73 Type de scellement
74 Eléments de scellement
75 Type de chiffrement 
76 Eléments de chiffrement
77 Type de signature
78 Sceau 
79 Signature
80 Accréditation
81 Accusé de réception de la signature
82 Deuxième signature
83 Deuxième accréditation
91 Message
99 Message libre
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
.C : Chaîne de caractères. 
.N : Numérique. Entier binaire sans signe.
.S : Symbolique. Comme N, c'est un entier binaire sans signe sauf que les
valeurs ont des significations particulières. Longueur de 8 bits.
.M : Masque de bits. Chaîne dans laquelle chaque bit a une signification
particulière. Longue de 8 à 16 bits. 
.D : Date et heure. AAMMJJ et hhmmss. Elle s'écrit sous forme d'une chaîne de caractères (voir ci-dessus) avec une longueur de 12 octets.
.A : Agrégat.

	
	
	 * 
	 */
	
	// TODO : hashmap of byte to a datatype for the affectation of value
	private static HashMap<Byte, String> byteToTypeMap = new HashMap<Byte, String>();
	static {
		byteToTypeMap.put(Byte.parseByte("1"), "C");
	}
	
	private Object value;
	
	public PI (byte id) {
		this.setId(id);
	}

	@Override
	public void setId(byte id) {
		this.id = id;
		// TODO : more ?
	}

	public byte getId() {
		return this.id;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return this.value;
	}
	
	public String toString() {
		String result = this.id + " : ";
		if (this.value instanceof Character[]) {
			for (int i = 0; i < ((Character[])this.value).length; i++) {
				result += (Character)((Character[]) this.value)[i];
			}
		}
		return result;
	}

}
