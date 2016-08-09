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
import com.omt.quikformz.model.AssignnmentInfoTable;
import com.omt.quikformz.util.DateUtil;

public class MySimpleArrayAdapterforMessages extends ArrayAdapter<AssignnmentInfoTable>{
	private final Context context;
	private List<AssignnmentInfoTable> information;


	public MySimpleArrayAdapterforMessages(Context context, List<AssignnmentInfoTable> information) {
		super(context, R.layout.message, information);
		this.context = context;
		this.information = information;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.message, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.textView1);
		ImageView imgView = (ImageView) rowView.findViewById(R.id.image);
		if(information != null && information.size() > 0){
		String text = information.get(position).text.toString();
		String msgStatus = information.get(position).status.toString();
		int length = text.length();
		if(length > 15){
			String splitText = text.substring(0, 14);
			text = splitText + ".....";
		}String time = DateUtil.convertLongToDate24hrs(Long.parseLong(information.get(position).ts.toString()));

		String fullString  = text +"\n"+time;
		
		textView.setText(fullString);
		if (msgStatus.equals("0")){
			imgView.setImageResource(R.drawable.unread);
		}
		else if(msgStatus.equals("1")){
			imgView.setImageResource(R.drawable.read);
		}
		}else{
        	Toast.makeText(context, "No Message Available", Toast.LENGTH_SHORT).show();
        }


		return rowView;
	}



}

