
package uk.co.samhogy.metroappwidget.model;

import uk.co.samhogy.metroappwidget.R;

public enum RailwayLines {
    GREEN (R.color.line_green),
    YELLOW (R.color.line_yellow),
    ALL (R.drawable.shape_all_lines);

    final int drawableResourceId;

    RailwayLines (int drawableResourceId) {
        this.drawableResourceId = drawableResourceId;
    }
}
