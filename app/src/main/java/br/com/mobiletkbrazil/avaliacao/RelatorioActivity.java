package br.com.mobiletkbrazil.avaliacao;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class RelatorioActivity extends AppCompatActivity implements OnClickListener {

    private DataPoint[] dados;
    private GraphView graph;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);

        dados = new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        };

        graph = findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dados);
        graph.addSeries(series);
        findViewById(R.id.bt_graph_bar).setOnClickListener(this);
        findViewById(R.id.bt_graph_line).setOnClickListener(this);
        findViewById(R.id.bt_graph_pie).setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        graph.removeAllSeries();
        graph.getSecondScale().removeAllSeries();
        graph.getSecondScale().setMinY(0);
        graph.getSecondScale().setMaxY(0);
        graph.getGridLabelRenderer().resetStyles();

        switch (v.getId()) {
            case R.id.bt_graph_line:
                LineGraphSeries<DataPoint> lineSeries = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(0, 2),
                        new DataPoint(1, -1),
                        new DataPoint(2, 4),
                        new DataPoint(3, 7),
                        new DataPoint(4, 3)
                });
                graph.addSeries(lineSeries);
                break;
            case R.id.bt_graph_bar:
                /*DataPoint[] points = new DataPoint[100];
                for (int i = 0; i < points.length; i++) {
                    points[i] = new DataPoint(i, Math.sin(i*0.5) * 20*(Math.random()*10+1));
                }*/

                BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                        new DataPoint(0, -1),
                        new DataPoint(1, 5),
                        new DataPoint(2, 3),
                        new DataPoint(3, 2),
                        new DataPoint(4, 6)
                });
                series.setSpacing(20);
                series.setDrawValuesOnTop(true);
                graph.addSeries(series);
                break;
            case R.id.bt_graph_pie:
                LineGraphSeries<DataPoint> firstSeries = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(0, 2),
                        new DataPoint(1, -1),
                        new DataPoint(2, 4),
                        new DataPoint(3, 7),
                        new DataPoint(4, 3)
                });
                graph.addSeries(firstSeries);

                LineGraphSeries<DataPoint> secondSeries = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(0, 25),
                        new DataPoint(1, 70),
                        new DataPoint(2, 58),
                        new DataPoint(3, 42),
                        new DataPoint(4, 80)
                });
                //graph.addSeries(secondSeries);
                graph.getSecondScale().addSeries(secondSeries);
                graph.getSecondScale().setMinY(0);
                graph.getSecondScale().setMaxY(100);
                secondSeries.setColor(Color.RED);
                graph.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.RED);
                break;
        }
    }

}
