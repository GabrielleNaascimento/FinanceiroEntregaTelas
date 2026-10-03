package view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class GraficoLinhaDashboard extends JPanel {

    public GraficoLinhaDashboard() {
        setLayout(new BorderLayout());

        CategoryDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(12, 12, 12, 12)
        ));

        add(chartPanel, BorderLayout.CENTER);
    }

    private JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createLineChart(
                "Receita x Despesa",
                "Mês",
                "Valor (R$)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        chart.setBackgroundPaint(Color.WHITE);
        chart.setTitle(new TextTitle("Receita x Despesa", new Font("SansSerif", Font.BOLD, 15)));
        chart.getLegend().setFrame(BlockBorder.NONE);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);


        LineAndShapeRenderer renderer = new LineAndShapeRenderer(true, true);
        renderer.setSeriesPaint(0, new Color(16, 185, 129)); // Receitas
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesPaint(1, new Color(239, 68, 68));  // Despesas
        renderer.setSeriesStroke(1, new BasicStroke(2.0f));
        plot.setRenderer(renderer);

        return chart;
    }

    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // addValue(valor, nomeDaSerie, categoria)
        dataset.addValue(120000, "Receitas", "Maio");
        dataset.addValue(135000, "Receitas", "Junho");
        dataset.addValue(128000, "Receitas", "Julho");
        dataset.addValue(142000, "Receitas", "Agosto");
        dataset.addValue(150000, "Receitas", "Setembro");
        dataset.addValue(156300, "Receitas", "Outubro");

        dataset.addValue(90000,  "Despesas", "Maio");
        dataset.addValue(98000,  "Despesas", "Junho");
        dataset.addValue(105000, "Despesas", "Julho");
        dataset.addValue(96000,  "Despesas", "Agosto");
        dataset.addValue(101000, "Despesas", "Setembro");
        dataset.addValue(98420,  "Despesas", "Outubro");

        return dataset;
    }
}