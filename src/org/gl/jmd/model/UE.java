package org.gl.jmd.model;

import java.io.Serializable;
import java.util.*;

import org.gl.jmd.model.enumeration.DecoupageYearType;
import org.gl.jmd.utils.NumberUtils;

/**
 * Classe repr�sentant une UE (d'une ann�e).
 * 
 * @author Jordi CHARPENTIER & Yoann VANHOESERLANDE
 */
public class UE implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Identifiant de l'UE.
	 */
	private int id;
	
	/**
	 * Le nom de l'UE.
	 */
	private String nom;
	
	/**
	 * Le type dont fait partie l'UE (semestre, ue, rien).
	 */
	private DecoupageYearType decoupageYearType;
	
	/**
	 * La liste des mati�res de l'UE.
	 */
	private ArrayList<Matiere> listeMatieres = new ArrayList<Matiere>();
	
	/**
	 * Les r�gles que devra respecter l'UE.
	 */
	private ArrayList<Regle> listeRegles = new ArrayList<Regle>();
	
	/**
	 * Constructeur par d�faut de la classe.
	 */
	public UE() {
		
	}
	
	/**
	 * M�thode permettant de calculer la moyenne de l'UE.
	 * 
	 * @return La moyenne de l'UE.
	 */
	public double getMoyenne() {
		double res = -1;
		
		int coeffGlobalUE = 0;
		double produitMatiereCoeff = 0.0;
		
		for (int i = 0; i < this.listeMatieres.size(); i++) {
			if (this.listeMatieres.get(i).getNoteFinale() == -2) {
				res = -2;
				
				return res;
			}
			
			coeffGlobalUE += this.listeMatieres.get(i).getCoefficient();
			produitMatiereCoeff += this.listeMatieres.get(i).getNoteFinale() * this.listeMatieres.get(i).getCoefficient();
		}
		
		res = produitMatiereCoeff / coeffGlobalUE;
		res = NumberUtils.round(res, 2);
		
		return res;
	}
	
	/* Getters. */
	
	/**
	 * M�thode retournant l'identifiant de l'UE.
	 * 
	 * @return L'identifiant de l'UE.
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * M�thode retournant le nom de l'UE.
	 * 
	 * @return Le nom de l'UE.
	 */
	public String getNom() {
		return this.nom;
	}
	
	/**
	 * M�thode retournant le type de d�coupage de l'UE (trimestre 1, semestre 2, ...).
	 * 
	 * @return Le type de d�coupage de l'UE (trimestre 1, semestre 2, ...).
	 */
	public DecoupageYearType getDecoupage() {
		return this.decoupageYearType;
	}
	
	/**
	 * M�thode retournant la liste des mati�res de l'UE.
	 * 
	 * @return La liste des mati�res de l'UE.
	 */
	public ArrayList<Matiere> getListeMatieres() {
		return this.listeMatieres;
	}
	
	/**
	 * M�thode retournant les r�gles de la mati�re.
	 * 
	 * @return Les r�gles de la mati�re.
	 */
	public ArrayList<Regle> getListeRegles() {
		return this.listeRegles;
	}
	
	/* Setters. */
	
	/**
	 * M�thode permettant de modifier l'identifiant de l'UE.
	 * 
	 * @param id Le nouvel identifiant de l'UE.
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * M�thode permettant de modifier le nom de l'UE.
	 * 
	 * @param nom Le nouveau nom de l'UE.
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	/**
	 * M�thode permettant de modifier le type de d�coupage de l'UE (trimestre 1, semestre 2, ...).
	 * 
	 * @param decoupageYearType Le nouveau type de d�coupage de l'UE (trimestre 1, semestre 2, ...).
	 */
	public void setDecoupage(DecoupageYearType decoupageYearType) {
		this.decoupageYearType = decoupageYearType;
	}
	
	/**
	 * M�thode permettant de modifier la liste des mati�res de l'UE.
	 * 
	 * @param listeMatieres La nouvelle liste des mati�res de l'UE.
	 */
	public void setListeMatieres(ArrayList<Matiere> listeMatieres) {
		this.listeMatieres = listeMatieres;
	}
	
	/**
	 * M�thode permettant de modifier les r�gles de la mati�re.
	 * 
	 * @param listeRegles Les nouvelle r�gles de la mati�re.
	 */
	public void setListeRegles(ArrayList<Regle> listeRegles) {
		this.listeRegles = listeRegles;
	}
}