package com.example.note2;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

public class AtySearch extends ListActivity {


	   @Override
	protected void onCreate(Bundle savedInstanceState) {
		   super.onCreate(savedInstanceState);
		   setContentView(R.layout.aty_search);
		   handleIntent(getIntent());
	}

	    @Override
	    protected void onNewIntent(Intent intent) {
	    	setIntent(intent);
	        handleIntent(intent);
	    }

	    private void handleIntent(Intent intent) {

	        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	            String query = intent.getStringExtra(SearchManager.QUERY);
	            doMySearch();
	        }
	    }

		private void doMySearch() {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
		}
	}



//handleIntent(getIntent());
/*	private void handleIntent(Intent intent){
		if(Intent.ACTION_SEARCH.equals(intent.getAction())){
			String query = intent.getStringExtra(SearchManager.QUERY);
			//use the query to search your data somehow
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}*/