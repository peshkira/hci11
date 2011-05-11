package com.questo.android;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.questo.android.common.Constants;
import com.questo.android.model.Place;

public class AddPlace extends Activity {

	private Place place;
	private ModelManager modelManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.place = new Place();
		this.modelManager = ((App)getApplication()).getModelManager();
		this.initPlace();
		this.initView();
	}
	
	private void initPlace(){
		Bundle extras = this.getIntent().getExtras();
		if((extras!=null) && (extras.containsKey(Constants.EXTRA_LATITUDE) && extras.containsKey(Constants.EXTRA_LONGITUDE))){
			this.place.setLatitude(extras.getDouble(Constants.EXTRA_LATITUDE));
			this.place.setLongitude(extras.getDouble(Constants.EXTRA_LONGITUDE));
		}
		else{
			LocationManager locationManager = (LocationManager) this
			.getSystemService(Context.LOCATION_SERVICE);
			Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if(currentLocation!=null){
				this.place.setLatitude(currentLocation.getLatitude());
				this.place.setLongitude(currentLocation.getLongitude());
			}
		}
	}

	private void initView() {
		setContentView(R.layout.add_place);
	}
	
	private void refreshObject(){
		EditText nameEdit = (EditText)findViewById(R.id.AddPlaceName);
		if(nameEdit!=null){
			this.place.setName(nameEdit.getText().toString());
		}
	}

	private class AddPlaceListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.AddPlaceCreateBtn) {
				AddPlace.this.refreshObject();
				AddPlace.this.modelManager.create(AddPlace.this.place,
						Place.class);
				AddPlace.this.finish();
			}
			if (v.getId() == R.id.AddPlaceCancelBtn) {
				AddPlace.this.finish();
			}
		}

	}
}
