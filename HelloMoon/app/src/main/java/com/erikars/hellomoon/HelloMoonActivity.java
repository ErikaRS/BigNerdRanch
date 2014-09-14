package com.erikars.hellomoon;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class HelloMoonActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return new HelloMoonFragment();
	}
}
