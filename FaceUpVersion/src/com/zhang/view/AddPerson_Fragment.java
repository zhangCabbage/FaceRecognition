package com.zhang.view;

import orange.zhang.app.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AddPerson_Fragment extends Fragment{

	private Context mContext;
	private View mBaseView;
	
	public AddPerson_Fragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = getActivity();
		mBaseView = inflater.inflate(R.layout.fragment_addperson, null);
		return mBaseView;
	}

	
}
