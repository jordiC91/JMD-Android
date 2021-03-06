package org.gl.jmd.model;

import java.io.Serializable;
import java.util.*;

import org.gl.jmd.model.enumeration.*;
import org.gl.jmd.utils.NumberUtils;

/**
 * Classe repr�sentant une ann�e (d'un dipl�me).
 * 
 * @author Jordi CHARPENTIER & Yoann VANHOESERLANDE
 */
public class Annee implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Identifiant de l'ann�e.
	 */
	private int id;

	/**
	 * Le nom de l'ann�e.
	 */
	private String nom;

	/**
	 * L'�tablissement de l'ann�e.
	 */
	private Etablissement etablissement;

	/**
	 * Le diplome o� l'ann�e est rattach�e.
	 */
	private Diplome d;

	/**
	 * Le d�coupage de l'ann�e.
	 */
	private DecoupageType decoupage;

	/**
	 * Bool�en permettant de savoir si l'ann�e est la derni�re du dipl�me.
	 */
	private boolean isLast;

	/**
	 * Bool�en permettant de savoir si l'ann�e est suivie par l'admin ou non.
	 */
	private boolean isFollowed;

	/**
	 * La liste des UE de l'ann�e.
	 */
	private List<UE> listeUE = new ArrayList<UE>();

	/**
	 * Constructeur par d�faut de la classe.
	 */
	public Annee() {

	}

	/**
	 * M�thode permettant de retourner l'avancement (en %) de l'ann�e.
	 * L'avancement correspond au pourcentage de mati�re ayant �t� "pass�es", donc ayant au moins une note.
	 * 
	 * @return L'avancement de l'ann�e.
	 */
	public float getAvancement() {
		float res = 0;

		float sommeCoeffEntrees = 0;
		float sommeCoeff = 0;

		for (int i = 0; i < this.listeUE.size(); i++) {
			for (int j = 0; j < this.listeUE.get(i).getListeMatieres().size(); j++) {
				if (this.listeUE.get(i).getListeMatieres().get(j).getNoteSession1().getNote() != -1.0 ) {
					sommeCoeffEntrees += this.listeUE.get(i).getListeMatieres().get(j).getCoefficient();
				}

				sommeCoeff += this.listeUE.get(i).getListeMatieres().get(j).getCoefficient();
			}
		} 

		res = (sommeCoeffEntrees / sommeCoeff) * 100;

		return res;
	}
	
	/**
	 * M�thode permettant de savoir si l'ann�e est d�faillante.
	 * Ici, une ann�e est d�faillante si au moins une de ses UE l'est.
	 * 
	 * @return <b>true</b> si l'ann�e est d�faillante.<br />
	 * <b>false</b> sinon.
	 */
	public boolean isDefaillant() {
		boolean res = false;
		
		for (int i = 0; i < this.listeUE.size(); i++) {			
			if (this.listeUE.get(i).isDefaillant()) {
				res = true;
				break;
			}
		}
		
		return res;
	}

	/**
	 * M�thode retournant la mention de l'ann�e.
	 * 
	 * @return La mention de l'ann�e.
	 */
	public String getMention() {
		if ((getMoyenne() >= 10.0) && (getMoyenne() < 12.0)) {
			return "Passable";
		} else if ((getMoyenne() >= 12.0) && (getMoyenne() < 14.0)) {
			return "Assez bien";
		} else if ((getMoyenne() >= 14.0) && (getMoyenne() < 16.0)) {
			return "Bien";
		} else if (getMoyenne() >= 16.0) {
			return "Tr�s bien";
		} else {
			return "Pas de mention";
		}
	}

	/**
	 * M�thode retournant la moyenne de l'ann�e.
	 * 
	 * @return La moyenne de l'ann�e.
	 */
	public double getMoyenne() {
		double res = -1.0;

		double resNULL = 0;
		float sommeCoeffEntrees = 0;
		boolean hasNote = false;

		for (int i = 0; i < this.listeUE.size(); i++) {				
			for (int j = 0; j < this.listeUE.get(i).getListeMatieres().size(); j++) {
				if (this.listeUE.get(i).getListeMatieres().get(j).getNoteFinale() != -1.0) {
					sommeCoeffEntrees += this.listeUE.get(i).getListeMatieres().get(j).getCoefficient();
					hasNote = true;
					resNULL += this.listeUE.get(i).getListeMatieres().get(j).getCoefficient() * this.listeUE.get(i).getListeMatieres().get(j).getNoteFinale();
				}
			}
		} 

		if (hasNote) {
			resNULL = resNULL / sommeCoeffEntrees;
			resNULL = NumberUtils.round(resNULL, 2);

			res = resNULL;
		} 

		return res;
	}

	/**
	 * 
	 * 
	 * @param d
	 * 
	 * @return
	 */
	public List<UE> getListeUEByDecoupage(DecoupageYearType d) {
		List<UE> l = new ArrayList<UE>();
		
		for (int i = 0; i < this.listeUE.size(); i++) {
			if (this.listeUE.get(i).getDecoupage() == d) {
				l.add(this.listeUE.get(i));
			}
		}
		
		return l;
	}

	/* Getters. */

	/**
	 * M�thode retournant l'identifiant de l'ann�e.
	 * 
	 * @return L'identifiant de l'ann�e.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * M�thode retournant le nom de l'ann�e.
	 * 
	 * @return Le nom de l'ann�e.
	 */
	public String getNom() {
		return this.nom;
	}

	/**
	 * M�thode retournant l'�tablissement de l'ann�e.
	 * 
	 * @return L'�tablissement de l'ann�e.
	 */
	public Etablissement getEtablissement() {
		return this.etablissement;
	}

	/**
	 * M�thode retournant le d�coupage de l'ann�e.
	 * 
	 * @return Le d�coupage de l'ann�e.
	 */
	public DecoupageType getDecoupage() {
		return this.decoupage;
	}

	/**
	 * M�thode retournant le dipl�me o� l'ann�e est rattach�e.
	 * 
	 * @return Le dipl�me o� l'ann�e est rattach�e.
	 */
	public Diplome getDiplome() {
		return this.d;
	}

	/**
	 * M�thode retournant le bool�en qui identifie si l'ann�e est la derni�re du dipl�me, ou non.
	 * 
	 * @return <b>true</b> si l'ann�e est la derni�re du dipl�me.
	 * <b>false</b> sinon.
	 */
	public boolean isLast() {
		return this.isLast;
	}

	/**
	 * M�thode retournant le bool�en qui identifie si l'ann�e est suivi, ou non.
	 * 
	 * @return <b>true</b> si l'ann�e est suivie.
	 * <b>false</b> sinon.
	 */
	public boolean isFollowed() {
		return this.isFollowed;
	}

	/**
	 * M�thode retournant la liste des UE de l'ann�e.
	 * 
	 * @return La liste des UE de l'ann�e.
	 */
	public List<UE> getListeUE() {
		return this.listeUE;
	}

	/* Setters. */

	/**
	 * M�thode permettant de modifier l'identifiant de l'ann�e.
	 * 
	 * @param id Le nouvel identifiant de l'ann�e.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * M�thode permettant de modifier le nom de l'ann�e.
	 * 
	 * @param nom Le nouveau nom de l'ann�e.
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * M�thode permettant de modifier l'�tablissement de l'ann�e.
	 * 
	 * @param etablissement Le nouvel �tablissement de l'ann�e.
	 */
	public void setEtablissement(Etablissement etablissement) {
		this.etablissement = etablissement;
	}

	/**
	 * M�thode permettant de modifier le d�coupage de l'ann�e.
	 * 
	 * @param decoupage Le nouveau d�coupage de l'ann�e.
	 */
	public void setDecoupage(DecoupageType decoupage) {
		this.decoupage = decoupage;
	}

	/**
	 * M�thode permettant de modifier le dipl�me o� l'ann�e est rattach�e.
	 * 
	 * @param d Le nouveau dipl�me o� l'ann�e sera rattach�e.
	 */
	public void setDiplome(Diplome d) {
		this.d = d;
	}

	/**
	 * M�thode permettant de modifier le bool�en qui identifie si l'ann�e est la derni�re du dipl�me, ou non.
	 * 
	 * @param isLast Le nouveau bool�en pour savoir si l'ann�e est la derni�re du dipl�me, ou non.
	 */
	public void setIsLast(boolean isLast) {
		this.isLast = isLast;
	}

	/**
	 * M�thode permettant de modifier le bool�en qui identifie si l'ann�e est suivie, ou non.
	 * 
	 * @param isLast Le nouveau bool�en pour savoir si l'ann�e est suivie, ou non.
	 */
	public void setIsFollowed(boolean isFollowed) {
		this.isFollowed = isFollowed;
	}

	/**
	 * M�thode permettant de modifier la liste des UE de l'ann�e.
	 * 
	 * @param listeUE La nouvelle liste des UE de l'ann�e.
	 */
	public void setListeUE(ArrayList<UE> listeUE) {
		this.listeUE = listeUE;
	}
}
