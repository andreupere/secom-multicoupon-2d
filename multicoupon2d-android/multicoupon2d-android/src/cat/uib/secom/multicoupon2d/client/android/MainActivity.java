package cat.uib.secom.multicoupon2d.client.android;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        Button btnGMTest = (Button) findViewById(R.id.buttonGMTest);
        btnGMTest.setOnClickListener( new View.OnClickListener() {
        		
        		public void onClick(View v) {
        			Intent intent = new Intent(v.getContext(), RegisterGroupManagerActivity.class);
        			startActivityForResult(intent, 0);
        		}
        } );
        
        Button btnITest = (Button) findViewById(R.id.buttonITest);
        btnITest.setOnClickListener( new View.OnClickListener() {
			
			
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), IssuingActivity.class);
				startActivityForResult(intent, 0);
			}
		});
        
        Button btnMRTest = (Button) findViewById(R.id.buttonMRTest);
        btnMRTest.setOnClickListener( new View.OnClickListener() {
			
			
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), MultiredeemActivity.class);
				startActivityForResult(intent, 0);
			}
		});
    
	}
}
