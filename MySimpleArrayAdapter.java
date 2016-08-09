package com.omt.quikformz.activity;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.omt.quikformz.R;
import com.omt.quikformz.logic.AssignmentInfo;
import com.omt.quikformz.model.RouteInfo;

public class MySimpleArrayAdapter extends ArrayAdapter<AssignmentInfo> {
	private final Context context;
	private List<AssignmentInfo> newAssignments;


	public MySimpleArrayAdapter(Context context, List<AssignmentInfo> assignments) {
		super(context, R.layout.rowlayouts, assignments);
		this.context = context;
		this.newAssignments = assignments;
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.rowlayouts, parent, false);

		TextView textView = (TextView) rowView.findViewById(R.id.textView1);
		TextView textView1 = (TextView) rowView.findViewById(R.id.textView2);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1);
        if(newAssignments != null && newAssignments.size() >0 ){
		if(newAssignments.get(position).assignStatus ==0){

		textView.setText(newAssignments.get(position).toString());
		textView1.setText("New");
		imageView.setImageResource(R.drawable.newassgn);
		}else {
		textView.setText(newAssignments.get(position).toString());
		imageView.setImageResource(R.drawable.inprogress);
		textView1.setText("In Progress");
		}
        } else{
        	Toast.makeText(context, "No Assignments Available", Toast.LENGTH_SHORT).show();
        }
		return rowView;
		}



		}
