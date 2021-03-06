package org.gl.jmd.view.etudiant;

import java.io.File;

import org.gl.jmd.R;
import org.gl.jmd.model.Annee;
import org.gl.jmd.utils.*;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.app.*;
import android.content.Intent;

/**
 * Activit� correspondant � la vue de stats d'une ann�e.
 * 
 * @author Jordi CHARPENTIER & Yoann VANHOESERLANDE
 */
public class StatsAnnee extends Activity {
	
	private Annee ann;
	
	private Activity activity;

	private Toast toast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.etudiant_stats_annee);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		
		ann = (Annee) getIntent().getSerializableExtra("annee");
		
		activity = this;
		toast = Toast.makeText(activity, "", Toast.LENGTH_SHORT);
		
		initTextViews();
	}
	
	private void initTextViews() {
		TextView tvMoyenne = (TextView) findViewById(R.id.stats_annee_moyenne_value);
		
		if (ann.getMoyenne() == -1.0) {
			tvMoyenne.setText("Aucune note");
		} else {
			tvMoyenne.setText(ann.getMoyenne() +  " / 20");
		}
		
		TextView tvAvancement = (TextView) findViewById(R.id.stats_annee_avancement_value);
		tvAvancement.setText(NumberUtils.round(ann.getAvancement(), 2) + " %");
		
		TextView tvMention = (TextView) findViewById(R.id.stats_annee_mention_value);
		tvMention.setText(ann.getMention());
	}
	
	public void back(View view) {
		finish();
	}
	
	/**
	 * M�thode d�clench�e lors d'un click sur le bouton de stats d'une ann�e.
	 * 
	 * @param view La vue lors du click sur le bouton de stats.
	 */
	public void export(View view) {		
		if (ann.getMoyenne() != -1.0) {
			ProgressDialog progress = new ProgressDialog(activity);
			progress.setMessage("Chargement...");
			new GenerateReport(progress).execute();	
		} else {
			toast.setText("Il faut au moins rentrer une note pour pouvoir g�n�rer un rapport au format PDF.");
			toast.show();
		}
	}
	
	/* Classe interne. */

	private class GenerateReport extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress;

		public GenerateReport(ProgressDialog progress) {
			this.progress = progress;
		}

		public void onPreExecute() {
			progress.show();
		}

		public void onPostExecute(Void unused) {
			progress.dismiss();
		}

		protected Void doInBackground(Void... arg0) {
			boolean res = PdfUtils.generateYearRapport(ann, activity);

			if (res) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File("/sdcard/cacheJMD/rapport-" + ann.getId() + ".pdf")), "application/pdf");
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(intent); 
			} else {
				toast.setText("Une erreur est survenue lors de la g�n�ration du rapport. Veuillez r�essayer.");
				toast.show();
			}
			
			return null;
		}
	}
}