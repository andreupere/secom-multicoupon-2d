package cat.uib.secom.multicoupon2d.client.android;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class IssuingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_issuing);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_issuing, menu);
		return true;
	}

}
