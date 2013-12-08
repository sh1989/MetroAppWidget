package uk.co.samhogy.metroappwidget;

import java.util.List;

import uk.co.samhogy.metroappwidget.model.Station;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StationListAdapter extends ArrayAdapter<Station> {

    final LayoutInflater inflater;
    final int height;
    final int width;
    final int padding;

    public StationListAdapter(Context context, List<Station> data) {
        super(context, 0, data);
        inflater = LayoutInflater.from(context);
        final float density = 
                context.getResources().getDisplayMetrics().density;
        height = (int) (8 * density);
        width = (int) (16 * density);
        padding = height;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(android.R.layout.simple_list_item_1, null);
        }

        final Station station = getItem(position);
        final TextView name = (TextView) view.findViewById(
                android.R.id.text1);

        Drawable d = getContext().getResources()
                .getDrawable(station.railwayLinesResourceId());
        d.setBounds(new Rect(0, 0, width, height));
        name.setCompoundDrawables(d, null, null, null);
        name.setCompoundDrawablePadding(padding);
        name.setText(station.name());

        return view;
    }

}
