package colombia.cartagena.moviluv3.Session;

import colombia.cartagena.moviluv3.MainActivity;
import colombia.cartagena.moviluv3.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

public class SessionActivity extends Activity {

	// User Session Manager Class
	UserSessionManager session;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_session);

		// Session class instance
		session = new UserSessionManager(getApplicationContext());

		// Check user login (this is the important point)
		// If User is not logged in , This will redirect user to LoginActivity
		// and finish current activity from activity stack.
		if (session.checkLogin()) {
			finish();
		} else {

			Intent intent = new Intent(SessionActivity.this, MainActivity.class);

			startActivity(intent);
		}

	}



}
