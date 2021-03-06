package org.gl.jmd.utils;

import java.io.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.gl.jmd.model.Annee;
import org.gl.jmd.model.enumeration.*;

import android.content.Context;
import android.graphics.*;
import android.graphics.Paint.Style;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.*;
import android.print.PrintAttributes;
import android.print.PrintAttributes.Margins;
import android.print.pdf.PrintedPdfDocument;

/**
 * Classe utilitaire permettant de manipuler des fichiers PDF.
 * 
 * @author Jordi CHARPENTIER & Yoann VANHOESERLANDE
 */
public class PdfUtils {

	/**
	 * Constructeur priv� de la classe.<br />
	 * Il est <i>private</i> pour emp�cher son instanciation.
	 */
	private PdfUtils() {

	}

	/**
	 * M�thode permettant de g�n�rer un rapport PDF � partir d'une ann�e.
	 * 
	 * @param ann L'ann�e utilis�e pour g�n�rer le rapport.
	 * @param c
	 * 
	 * @return <b>true</b> si le rapport a bien �t� g�n�r� et sauv�.
	 * <b>false</b> sinon.
	 */
	public static boolean generateYearRapport(Annee ann, Context c) {
		boolean res = false;
		
		PrintAttributes printAttrs = new PrintAttributes.Builder().
				setColorMode(PrintAttributes.COLOR_MODE_MONOCHROME).
				setMediaSize(PrintAttributes.MediaSize.ISO_A4).
				setMinMargins(Margins.NO_MARGINS).
				build();
		
		PdfDocument document = new PrintedPdfDocument(c, printAttrs);
		
		PageInfo pageInfo = new PageInfo.Builder(595, 842, 1).create();
		PageInfo pageInfo2 = new PageInfo.Builder(595, 842, 1).create();
		PageInfo pageInfo3 = new PageInfo.Builder(595, 842, 1).create();

		if (ann.getMoyenne() != -1.0) {
			// Apprentissage
			if (ann.getDecoupage() == DecoupageType.NULL) {
				Page page = document.startPage(pageInfo);
				
				Paint paint = new Paint(); 
				paint.setColor(Color.WHITE); 
				paint.setStyle(Style.FILL); 
				
				page.getCanvas().drawPaint(paint); 

				paint.setColor(Color.BLACK); 
				paint.setTextSize(10); 
				
				page.getCanvas().drawText(ann.getEtablissement().getNom(), 50, 40, paint);
				page.getCanvas().drawText("Page 1 / 1", 500, 40, paint);
				
				paint.setTextSize(12); 
				
				paint.setFakeBoldText(true);
				paint.setTextSize(18);
				paint.setStyle(Paint.Style.STROKE);
				page.getCanvas().drawText("RELEVE DE NOTES ET RESULTATS", 160, 70, paint);
				paint.setStyle(Style.FILL); 
				paint.setFakeBoldText(false);
				paint.setTextSize(12);
				page.getCanvas().drawText("Inscrit en : " + ann.getNom() + ".", 50, 100, paint);
				
				page.getCanvas().drawText("A obtenu les notes suivantes :", 50, 115, paint); 
				
				int startY = 145;
			
				for (int i = 0; i < ann.getListeUE().size(); i++) {
					paint.setFakeBoldText(true);
					
					String txtUE = ann.getListeUE().get(i).getNom() + " : ";
					
					if (ann.getListeUE().get(i).getMoyenne() != -1.0) {
						txtUE += ann.getListeUE().get(i).getMoyenne() + "/20";
						
						if (ann.getListeUE().get(i).isDefaillant()) {
							txtUE += " (Ajourn�)";
						} else if (ann.getListeUE().get(i).getMoyenne() >= 10.0) {
							txtUE += " (Admis)";
						} else if (ann.getListeUE().get(i).getMoyenne() < 10.0) {
							txtUE += " (Non valid�)";
						}
					} 
					
					page.getCanvas().drawText(txtUE, 50, startY, paint); 
					
					for (int j = 0; j < ann.getListeUE().get(i).getListeMatieres().size(); j++) {
						paint.setFakeBoldText(false);
						startY += 15;
						String txtMatiere = ann.getListeUE().get(i).getListeMatieres().get(j).getNom() + " (coefficient " + ann.getListeUE().get(i).getListeMatieres().get(j).getCoefficient() + ")"; 
						
						if (ann.getListeUE().get(i).getListeMatieres().get(j).getNoteFinale() != -1.0) {
							page.getCanvas().drawText(txtMatiere + " : " + ann.getListeUE().get(i).getListeMatieres().get(j).getNoteFinale() + "/20", 50, startY, paint); 
						} else {
							page.getCanvas().drawText(txtMatiere + " : Aucune note.", 50, startY, paint); 
						}
					}
					
					startY += 20;
				}
				
				startY += 10;
				
				paint.setFakeBoldText(true);
				page.getCanvas().drawText("Moyenne g�n�rale : ", 50, startY, paint);
				paint.setFakeBoldText(false);
				page.getCanvas().drawText(ann.getMoyenne() + "/20", 165, startY, paint);
				startY += 15;
				paint.setFakeBoldText(true);
				page.getCanvas().drawText("R�sultat : ", 50, startY, paint);
				paint.setFakeBoldText(false);
				
				String txtRes = "";
				
				if (ann.isDefaillant()) {
					txtRes = "D�faillant";
				} else if (ann.getMoyenne() >= 10.0) {
					txtRes = "Admis (" + ann.getMention() + ")";
				} else if (ann.getMoyenne() < 10.0) {
					txtRes = "Non valid�";
				}
				
				page.getCanvas().drawText(txtRes, 110, startY, paint);
				startY += 30;
	
				page.getCanvas().drawText("Fait � " + ann.getEtablissement().getVille() + ", le " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + ".", 50, startY, paint);	
			
				paint.setTextSize(10);
				page.getCanvas().drawText("Avis important : Aucun duplicata ne sera fourni.", 50, 802, paint);
				
				document.finishPage(page);
			} else if (ann.getDecoupage() == DecoupageType.SEMESTRE) { 
				Page page = document.startPage(pageInfo);
				
				Paint paint = new Paint(); 
				paint.setColor(Color.WHITE); 
				paint.setStyle(Style.FILL); 
				
				page.getCanvas().drawPaint(paint); 

				paint.setColor(Color.BLACK); 
				paint.setTextSize(10); 
				
				page.getCanvas().drawText(ann.getEtablissement().getNom(), 50, 40, paint);
				page.getCanvas().drawText("Page 1 / 2", 500, 40, paint);
				
				paint.setTextSize(12); 
				paint.setFakeBoldText(true);
				paint.setTextSize(18);
				paint.setStyle(Paint.Style.STROKE);
				page.getCanvas().drawText("RELEVE DE NOTES ET RESULTATS", 160, 70, paint);
				paint.setStyle(Style.FILL); 
				paint.setFakeBoldText(false);
				paint.setTextSize(12);
				page.getCanvas().drawText("Inscrit en : " + ann.getNom() + ".", 50, 100, paint);
				
				page.getCanvas().drawText("A obtenu les notes suivantes :", 50, 115, paint); 
				
				paint.setUnderlineText(true);
				page.getCanvas().drawText("Semestre 1", 50, 145, paint);
				paint.setUnderlineText(false);
				
				// Semestre 1
				int startY = 175;
			
				for (int i = 0; i < ann.getListeUE().size(); i++) {
					if (ann.getListeUE().get(i).getDecoupage() == DecoupageYearType.SEM1) {
						paint.setFakeBoldText(true);
						
						String txtUE = ann.getListeUE().get(i).getNom() + " : ";
						
						if (ann.getListeUE().get(i).getMoyenne() != -1.0) {
							txtUE += ann.getListeUE().get(i).getMoyenne() + "/20";
							
							if (ann.getListeUE().get(i).isDefaillant()) {
								txtUE += " (Ajourn�)";
							} else if (ann.getListeUE().get(i).getMoyenne() >= 10.0) {
								txtUE += " (Admis)";
							} else if (ann.getListeUE().get(i).getMoyenne() < 10.0) {
								txtUE += " (Non valid�)";
							}
						} 
						
						page.getCanvas().drawText(txtUE, 50, startY, paint); 
						
						for (int j = 0; j < ann.getListeUE().get(i).getListeMatieres().size(); j++) {
							paint.setFakeBoldText(false);
							startY += 15;
							String txtMatiere = ann.getListeUE().get(i).getListeMatieres().get(j).getNom() + " (coefficient " + ann.getListeUE().get(i).getListeMatieres().get(j).getCoefficient() + ")"; 
							
							if (ann.getListeUE().get(i).getListeMatieres().get(j).getNoteFinale() != -1.0) {
								page.getCanvas().drawText(txtMatiere + " : " + ann.getListeUE().get(i).getListeMatieres().get(j).getNoteFinale() + "/20", 50, startY, paint); 
							} else {
								page.getCanvas().drawText(txtMatiere + " : Aucune note.", 50, startY, paint); 
							}
						}
						
						startY += 20;
					}
				}
				
				paint.setTextSize(10);
				page.getCanvas().drawText("Avis important : Aucun duplicata ne sera fourni.", 50, 802, paint);
				
				document.finishPage(page);
				
				// Semestre 2
				Page page2 = document.startPage(pageInfo2);
				
				Paint paint2 = new Paint(); 
				paint2.setColor(Color.WHITE); 
				paint2.setStyle(Style.FILL); 
				
				page2.getCanvas().drawPaint(paint2); 

				paint2.setColor(Color.BLACK); 
				
				paint2.setTextSize(10);
				page2.getCanvas().drawText(ann.getEtablissement().getNom(), 50, 40, paint2);
				page2.getCanvas().drawText("Page 2 / 2", 500, 40, paint);
				
				paint2.setTextSize(12); 
				
				paint2.setUnderlineText(true);
				page2.getCanvas().drawText("Semestre 2", 50, 70, paint2);
				paint2.setUnderlineText(false);
				
				int startY_2 = 100;
				
				for (int i = 0; i < ann.getListeUE().size(); i++) {
					if (ann.getListeUE().get(i).getDecoupage() == DecoupageYearType.SEM2) {
						paint2.setFakeBoldText(true);
						
						String txtUE = ann.getListeUE().get(i).getNom() + " : ";
						
						if (ann.getListeUE().get(i).getMoyenne() != -1.0) {
							txtUE += ann.getListeUE().get(i).getMoyenne() + "/20";
							
							if (ann.getListeUE().get(i).isDefaillant()) {
								txtUE += " (Ajourn�)";
							} else if (ann.getListeUE().get(i).getMoyenne() >= 10.0) {
								txtUE += " (Admis)";
							} else if (ann.getListeUE().get(i).getMoyenne() < 10.0) {
								txtUE += " (Non valid�)";
							}
						} 
						
						page2.getCanvas().drawText(txtUE, 50, startY_2, paint2); 
						
						for (int j = 0; j < ann.getListeUE().get(i).getListeMatieres().size(); j++) {
							paint2.setFakeBoldText(false);
							startY_2 += 15;
							String txtMatiere = ann.getListeUE().get(i).getListeMatieres().get(j).getNom() + " (coefficient " + ann.getListeUE().get(i).getListeMatieres().get(j).getCoefficient() + ")"; 
							
							if (ann.getListeUE().get(i).getListeMatieres().get(j).getNoteFinale() != -1.0) {
								page2.getCanvas().drawText(txtMatiere + " : " + ann.getListeUE().get(i).getListeMatieres().get(j).getNoteFinale() + "/20", 50, startY_2, paint2); 
							} else {
								page2.getCanvas().drawText(txtMatiere + " : Aucune note.", 50, startY_2, paint2); 
							}
						}
						
						startY_2 += 20;
					}
				}
				
				startY_2 += 10;
				
				paint2.setFakeBoldText(true);
				page2.getCanvas().drawText("Moyenne g�n�rale : ", 50, startY_2, paint2);
				paint2.setFakeBoldText(false);
				page2.getCanvas().drawText(ann.getMoyenne() + "/20", 165, startY_2, paint2);
				startY_2 += 15;
				paint2.setFakeBoldText(true);
				page2.getCanvas().drawText("R�sultat : ", 50, startY_2, paint2);
				paint2.setFakeBoldText(false);
				
				String txtRes = "";
				
				if (ann.isDefaillant()) {
					txtRes = "D�faillant";
				} else if (ann.getMoyenne() >= 10.0) {
					txtRes = "Admis (" + ann.getMention() + ")";
				} else if (ann.getMoyenne() < 10.0) {
					txtRes = "Non valid�";
				}
				
				page2.getCanvas().drawText(txtRes, 110, startY_2, paint2);
				startY_2 += 30;
	
				page2.getCanvas().drawText("Fait � " + ann.getEtablissement().getVille() + ", le " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + ".", 50, startY_2, paint2);	
			
				paint2.setTextSize(10);
				page2.getCanvas().drawText("Avis important : Aucun duplicata ne sera fourni.", 50, 802, paint2);
				
				document.finishPage(page2);
			} else if (ann.getDecoupage() == DecoupageType.TRIMESTRE) {				
				Page page = document.startPage(pageInfo);
				
				Paint paint = new Paint(); 
				paint.setColor(Color.WHITE); 
				paint.setStyle(Style.FILL); 
				
				page.getCanvas().drawPaint(paint); 

				paint.setColor(Color.BLACK); 
				paint.setTextSize(10); 
				
				page.getCanvas().drawText(ann.getEtablissement().getNom(), 50, 40, paint);
				page.getCanvas().drawText("Page 1 / 3", 500, 40, paint);
				
				paint.setTextSize(12); 
				paint.setFakeBoldText(true);
				paint.setTextSize(18);
				paint.setStyle(Paint.Style.STROKE);
				page.getCanvas().drawText("RELEVE DE NOTES ET RESULTATS", 160, 70, paint);
				paint.setStyle(Style.FILL); 
				paint.setFakeBoldText(false);
				paint.setTextSize(12);
				page.getCanvas().drawText("Inscrit en : " + ann.getNom() + ".", 50, 100, paint);
				
				page.getCanvas().drawText("A obtenu les notes suivantes :", 50, 115, paint); 
				
				paint.setUnderlineText(true);
				page.getCanvas().drawText("Trimestre 1", 50, 145, paint);
				paint.setUnderlineText(false);
				
				// Trimestre 1
				int startY = 175;
			
				for (int i = 0; i < ann.getListeUE().size(); i++) {
					if (ann.getListeUE().get(i).getDecoupage() == DecoupageYearType.TRI1) {
						paint.setFakeBoldText(true);
						String txtUE = ann.getListeUE().get(i).getNom() + " : ";
						
						if (ann.getListeUE().get(i).getMoyenne() != -1.0) {
							txtUE += ann.getListeUE().get(i).getMoyenne() + "/20";
							
							if (ann.getListeUE().get(i).isDefaillant()) {
								txtUE += " (Ajourn�)";
							} else if (ann.getListeUE().get(i).getMoyenne() >= 10.0) {
								txtUE += " (Admis)";
							} else if (ann.getListeUE().get(i).getMoyenne() < 10.0) {
								txtUE += " (Non valid�)";
							}
						} 
						
						page.getCanvas().drawText(txtUE, 50, startY, paint); 
						
						for (int j = 0; j < ann.getListeUE().get(i).getListeMatieres().size(); j++) {
							paint.setFakeBoldText(false);
							startY += 15;
							String txtMatiere = ann.getListeUE().get(i).getListeMatieres().get(j).getNom() + " (coefficient " + ann.getListeUE().get(i).getListeMatieres().get(j).getCoefficient() + ")"; 
							
							if (ann.getListeUE().get(i).getListeMatieres().get(j).getNoteFinale() != -1.0) {
								page.getCanvas().drawText(txtMatiere + " : " + ann.getListeUE().get(i).getListeMatieres().get(j).getNoteFinale() + "/20", 50, startY, paint); 
							} else {
								page.getCanvas().drawText(txtMatiere + " : Aucune note.", 50, startY, paint); 
							}
						}
						
						startY += 20;
					}
				}
				
				paint.setTextSize(10);
				page.getCanvas().drawText("Avis important : Aucun duplicata ne sera fourni.", 50, 802, paint);
				
				document.finishPage(page);
				
				// Trimestre 2
				Page page2 = document.startPage(pageInfo2);
				
				Paint paint2 = new Paint(); 
				paint2.setColor(Color.WHITE); 
				paint2.setStyle(Style.FILL); 
				
				page2.getCanvas().drawPaint(paint2); 

				paint2.setColor(Color.BLACK); 
				
				paint2.setTextSize(10);
				page2.getCanvas().drawText(ann.getEtablissement().getNom(), 50, 40, paint2);
				page2.getCanvas().drawText("Page 2 / 3", 500, 40, paint2);
				
				paint2.setTextSize(12); 
				
				paint2.setUnderlineText(true);
				page2.getCanvas().drawText("Trimestre 2", 50, 70, paint2);
				paint2.setUnderlineText(false);
				
				int startY_2 = 100;
				
				for (int i = 0; i < ann.getListeUE().size(); i++) {
					if (ann.getListeUE().get(i).getDecoupage() == DecoupageYearType.TRI2) {
						paint2.setFakeBoldText(true);
						
						String txtUE = ann.getListeUE().get(i).getNom() + " : ";
						
						if (ann.getListeUE().get(i).getMoyenne() != -1.0) {
							txtUE += ann.getListeUE().get(i).getMoyenne() + "/20";
							
							if (ann.getListeUE().get(i).isDefaillant()) {
								txtUE += " (Ajourn�)";
							} else if (ann.getListeUE().get(i).getMoyenne() >= 10.0) {
								txtUE += " (Admis)";
							} else if (ann.getListeUE().get(i).getMoyenne() < 10.0) {
								txtUE += " (Non valid�)";
							}
						} 
						
						page2.getCanvas().drawText(txtUE, 50, startY_2, paint2); 
						
						for (int j = 0; j < ann.getListeUE().get(i).getListeMatieres().size(); j++) {
							paint2.setFakeBoldText(false);
							startY_2 += 15;
							String txtMatiere = ann.getListeUE().get(i).getListeMatieres().get(j).getNom() + " (coefficient " + ann.getListeUE().get(i).getListeMatieres().get(j).getCoefficient() + ")"; 
							
							if (ann.getListeUE().get(i).getListeMatieres().get(j).getNoteFinale() != -1.0) {
								page2.getCanvas().drawText(txtMatiere + " : " + ann.getListeUE().get(i).getListeMatieres().get(j).getNoteFinale() + "/20", 50, startY_2, paint2); 
							} else {
								page2.getCanvas().drawText(txtMatiere + " : Aucune note.", 50, startY_2, paint2); 
							}
						}
						
						startY_2 += 20;
					}
				} 
				
				paint2.setTextSize(10);
				page2.getCanvas().drawText("Avis important : Aucun duplicata ne sera fourni.", 50, 802, paint2);
				
				document.finishPage(page2);
				
				// Trimestre 3
				Page page3 = document.startPage(pageInfo3);
				
				Paint paint3 = new Paint(); 
				paint3.setColor(Color.WHITE); 
				paint3.setStyle(Style.FILL); 
				
				page3.getCanvas().drawPaint(paint3); 

				paint3.setColor(Color.BLACK); 
				
				paint3.setTextSize(10);
				page3.getCanvas().drawText(ann.getEtablissement().getNom(), 50, 40, paint3);
				page3.getCanvas().drawText("Page 3 / 3", 500, 40, paint3);
				
				paint3.setTextSize(12); 
				
				paint3.setUnderlineText(true);
				page3.getCanvas().drawText("Trimestre 3", 50, 70, paint3);
				paint3.setUnderlineText(false);
				
				int startY_3 = 100;
				
				for (int i = 0; i < ann.getListeUE().size(); i++) {
					if (ann.getListeUE().get(i).getDecoupage() == DecoupageYearType.TRI3) {
						paint3.setFakeBoldText(true);
						
						String txtUE = ann.getListeUE().get(i).getNom() + " : ";
						
						if (ann.getListeUE().get(i).getMoyenne() != -1.0) {
							txtUE += ann.getListeUE().get(i).getMoyenne() + "/20";
							
							if (ann.getListeUE().get(i).isDefaillant()) {
								txtUE += " (Ajourn�)";
							} else if (ann.getListeUE().get(i).getMoyenne() >= 10.0) {
								txtUE += " (Admis)";
							} else if (ann.getListeUE().get(i).getMoyenne() < 10.0) {
								txtUE += " (Non valid�)";
							}
						} 
						
						page3.getCanvas().drawText(txtUE, 50, startY_3, paint3); 
						
						for (int j = 0; j < ann.getListeUE().get(i).getListeMatieres().size(); j++) {
							paint3.setFakeBoldText(false);
							startY_3 += 15;
							String txtMatiere = ann.getListeUE().get(i).getListeMatieres().get(j).getNom() + " (coefficient " + ann.getListeUE().get(i).getListeMatieres().get(j).getCoefficient() + ")"; 
							
							if (ann.getListeUE().get(i).getListeMatieres().get(j).getNoteFinale() != -1.0) {
								page3.getCanvas().drawText(txtMatiere + " : " + ann.getListeUE().get(i).getListeMatieres().get(j).getNoteFinale() + "/20", 50, startY_3, paint3); 
							} else {
								page3.getCanvas().drawText(txtMatiere + " : Aucune note.", 50, startY_3, paint3); 
							}
						}
						
						startY_3 += 20;
					}
				} 
				
				startY_3 += 10;
				
				paint3.setFakeBoldText(true);
				page3.getCanvas().drawText("Moyenne g�n�rale : ", 50, startY_3, paint3);
				paint3.setFakeBoldText(false);
				page3.getCanvas().drawText(ann.getMoyenne() + "/20", 165, startY_3, paint3);
				startY_3 += 15;
				paint3.setFakeBoldText(true);
				page3.getCanvas().drawText("R�sultat : ", 50, startY_3, paint3);
				paint3.setFakeBoldText(false);
				
				String txtRes = "";
				
				if (ann.isDefaillant()) {
					txtRes = "D�faillant";
				} else if (ann.getMoyenne() >= 10.0) {
					txtRes = "Admis (" + ann.getMention() + ")";
				} else if (ann.getMoyenne() < 10.0) {
					txtRes = "Non valid�";
				}
				
				page3.getCanvas().drawText(txtRes, 110, startY_3, paint3);
				startY_3 += 30;
	
				page3.getCanvas().drawText("Fait � " + ann.getEtablissement().getVille() + ", le " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + ".", 50, startY_3, paint3);	
			
				paint3.setTextSize(10);
				page3.getCanvas().drawText("Avis important : Aucun duplicata ne sera fourni.", 50, 802, paint3);
				
				document.finishPage(page3); 
			}
			
			// Ecriture du rapport.
			try {
				document.writeTo(new FileOutputStream(new File("/sdcard/cacheJMD/rapport-" + ann.getId() + ".pdf")));
				res = true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				res = false;
			} catch (IOException e) {
				e.printStackTrace();
				res = false;
			}
		} 
		
		return res;
	}
}