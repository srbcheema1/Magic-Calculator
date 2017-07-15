package cheema.calculater.mymagic.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cheema.calculater.R;
import cheema.calculater.mymagic.pojo_classes.KeylogPojo;


/**
 * Created by srb on 7/7/17.
 */

public class Keytables_list_adapter extends ArrayAdapter {
    public Keytables_list_adapter(@NonNull Context context, @LayoutRes int resource, ArrayList<KeylogPojo> arrayList) {
        super(context, resource);
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return super.getPosition(item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            LayoutInflater layoutInflater = (LayoutInflater.from(parent.getContext()));
            convertView = layoutInflater.inflate(R.layout.item,parent,false);
        }
        TextView KeyTv=(TextView) convertView.findViewById(R.id.keyTv);
        TextView ansTv = (TextView) convertView.findViewById(R.id.ansTv);

        KeylogPojo obj = arrayList.get(position);
        KeyTv.setText(obj.getKey());
        ansTv.setText(obj.getAns());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //variables
    ArrayList<KeylogPojo> arrayList;
}
