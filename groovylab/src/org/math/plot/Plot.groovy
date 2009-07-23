package org.math.plot;

import org.math.array.Matrix
import org.math.plot.*


class Plot {

	static HashMap<PlotPanel,FrameView> plots
	
	static PlotPanel currentplot
	
	static boolean new_figure = false
		
	static boolean AUTO_FRAME = true 
	
	private static initplots() {
		if (plots==null) plots=new HashMap<PlotPanel,FrameView>()
	}
	
	private static initplot3d() {
		initplots()
		if (!(currentplot instanceof Plot3DPanel) || new_figure) {
			currentplot = new Plot3DPanel()
			currentplot.setLegendOrientation("SOUTH")
			plots.put(currentplot,AUTO_FRAME ? new FrameView(currentplot):null)
		}
		if (new_figure) 
			new_figure=false
		
	}
	
	private static initplot2d() {
		initplots()
		if (!(currentplot instanceof Plot2DPanel) || new_figure) {
			currentplot = new Plot2DPanel()
			currentplot.setLegendOrientation("SOUTH")
			plots.put(currentplot,AUTO_FRAME ?new FrameView(currentplot):null)
		}
		if (new_figure) 
			new_figure=false
		
	}
	
	
	static PlotPanel figure(int i) {
		return plots.keySet().toArray()[i-1]
	}
	
	static int figure() {
		new_figure=true
		return plots.size()+1
	}
	
	static void close() {
		((FrameView)plots.get(currentplot)).setVisible(false)
		plots.remove(currentplot)
		currentplot = plots.keySet().toArray()[plots.size()-1]
	}
	
	static void close(int i) {
		((FrameView)plots.get(figure(i))).setVisible(false)
		plots.remove(figure(i))
		currentplot = plots.keySet().toArray()[plots.size()-1]
	}
	
	private static int asRowLength(Matrix x) {
		if (x.getRowsNumber()==1)
			return x.getColumnsNumber()
		else if (x.getColumnsNumber()==1)
			return x.getRowsNumber()
		else 
			throw new IllegalArgumentException("Impossible to convert Matrix to 1D array :\n"+x)
	}
	
	private static double[] asRow(Matrix x, int length) {
		if (x.getRowsNumber()!=length && x.getColumnsNumber()!=length) 
			throw new IllegalArgumentException("Impossible to convert Matrix to 1D array of length "+length+" :\n"+x)
		
		if (x.getRowsNumber()==1)
			return x.getRowRef(1)
		else 
			return x.getColumnCopyasRow(1)
	}
	
	
	//////////////
	// 3D plots //
	//////////////
	
	static PlotPanel plot3d_scatter(String name,Matrix xyz) {
		initplot3d()
		((Plot3DPanel)currentplot).addScatterPlot(name,xyz.getRef())
		return currentplot
	}
	
	static PlotPanel plot3d_scatter(String name,Matrix x,Matrix y, Matrix z) {
		initplot3d()
		int l=asRowLength(x)
		((Plot3DPanel)currentplot).addScatterPlot(name,asRow(x,l),asRow(y,l),asRow(z,l))
		return currentplot
	}
	
	static PlotPanel plot3d_line(String name,Matrix xyz) {
		initplot3d()
		((Plot3DPanel)currentplot).addLinePlot(name,xyz.getRef())
		return currentplot
	}
	
	static PlotPanel plot3d_line(String name,Matrix x,Matrix y, Matrix z) {
		initplot3d()
		int l=asRowLength(x)
		((Plot3DPanel)currentplot).addLinePlot(name,asRow(x,l),asRow(y,l),asRow(z,l))
		return currentplot
	}
	
	static PlotPanel plot3d_bar(String name,Matrix xyz) {
		initplot3d()
		((Plot3DPanel)currentplot).addBarPlot(name,xyz.getRef())
		return currentplot
	}
	
	static PlotPanel plot3d_bar(String name,Matrix x,Matrix y, Matrix z) {
		initplot3d()
		int l=asRowLength(x)
		((Plot3DPanel)currentplot).addBarPlot(name,asRow(x,l),asRow(y,l),asRow(z,l))
		return currentplot
	}
	
	static PlotPanel plot3d_cloud(String name,Matrix sample, int slices_x,int slices_y,int slices_z) {
		initplot3d()
		((Plot3DPanel)currentplot).addCloudPlot(name,sample.getRef(),slices_x, slices_y, slices_z)
		return currentplot
	}
	
	static PlotPanel plot3d_histogram(String name,Matrix sample, int slices_x,int slices_y) {
		initplot3d()
		((Plot3DPanel)currentplot).addHistogramPlot(name,sample.getRef(),slices_x, slices_y)
		return currentplot
	}
	
	static PlotPanel plot3d_grid(String name,Matrix x, Matrix y, Matrix z) {
		initplot3d()
		if ( z.getRowsNumber()!=asRowLength(y))
			throw new IllegalArgumentException("Matrix z=\n"+z+"\n has not "+asRowLength(y)+" rows, like y")
		
		if (z.getColumnsNumber()!=asRowLength(x))
			throw new IllegalArgumentException("Matrix z=\n"+z+"\n has not "+asRowLength(x)+" columns, like x")
		
		((Plot3DPanel)currentplot).addGridPlot(name,asRow(x,z.getColumnsNumber()),asRow(y,z.getRowsNumber()),z.getRef())
		return currentplot
	}
	
	static PlotPanel plot3d_grid(String name,Matrix xyz) {
		initplot3d()
		((Plot3DPanel)currentplot).addGridPlot(name,xyz.getRef())
		return currentplot
	}
	
	static PlotPanel plot3d(String name,Matrix xyz,String type="SCATTER") {
		if (type.equals("SCATTER"))
			return plot3d_scatter(name,xyz)
		else if (type.equals("LINE"))
			return plot3d_line(name,xyz)
		else if (type.equals("BAR"))
			return plot3d_bar(name,xyz)
		else if (type.equals("GRID"))
			return plot3d_grid(name,xyz) 
		else throw new IllegalArgumentException("Plot type "+type+" unknown")	
	}
	
	static PlotPanel plot3d(String name,Matrix x, Matrix y, Matrix z,String type="SCATTER") {
		if (type.equals("SCATTER"))
			return plot3d_scatter(name,x,y,z)
		else if (type.equals("LINE"))
			return plot3d_line(name,x,y,z)
		else if (type.equals("BAR"))
			return plot3d_bar(name,x,y,z)
		else if (type.equals("GRID"))
			return plot3d_grid(name,x,y,z) 
		else throw new IllegalArgumentException("Plot type "+type+" unknown")	
	}
	
	static PlotPanel plot3d(String name,Matrix sample,int slices_x,int slices_y,int slices_z,String type="CLOUD") {
		if (type.equals("CLOUD"))
			return plot3d_cloud(name,sample, slices_x, slices_y,slices_z)
		else throw new IllegalArgumentException("Plot type "+type+" unknown")	
	}
	
	static PlotPanel plot3d(String name,Matrix sample,int slices_x,int slices_y,String type="HISTOGRAM") {
		if (type.equals("HISTOGRAM"))
			return plot3d_histogram(name,sample, slices_x, slices_y)
		else throw new IllegalArgumentException("Plot type "+type+" unknown")	
	}	
	
	//////////////
	// 2D plots //
	//////////////
	
	static PlotPanel plot2d_scatter(String name,Matrix xy) {
		initplot2d()
		((Plot2DPanel)currentplot).addScatterPlot(name,xy.getRef())
		return currentplot
	}
	
	static PlotPanel plot2d_scatter(String name,Matrix x,Matrix y) {
		initplot2d()
		int l=asRowLength(x)
		((Plot2DPanel)currentplot).addScatterPlot(name,asRow(x,l),asRow(y,l))
		return currentplot
	}
	
	static PlotPanel plot2d_line(String name,Matrix xy) {
		initplot2d()
		((Plot2DPanel)currentplot).addLinePlot(name,xy.getRef())
		return currentplot
	}
	
	static PlotPanel plot2d_line(String name,Matrix x,Matrix y) {
		initplot2d()
		int l=asRowLength(x)
		((Plot2DPanel)currentplot).addLinePlot(name,asRow(x,l),asRow(y,l))
		return currentplot
	}
	
	static PlotPanel plot2d_staircase(String name,Matrix xy) {
		initplot2d()
		((Plot2DPanel)currentplot).addStaircasePlot(name,xy.getRef())
		return currentplot
	}
	
	static PlotPanel plot2d_staircase(String name,Matrix x,Matrix y) {
		initplot2d()
		int l=asRowLength(x)
		((Plot2DPanel)currentplot).addStaircasePlot(name,asRow(x,l),asRow(y,l))
		return currentplot
	}
	
	static PlotPanel plot2d_bar(String name,Matrix xy) {
		initplot2d()
		((Plot2DPanel)currentplot).addBarPlot(name,xy.getRef())
		return currentplot
	}
	
	static PlotPanel plot2d_bar(String name,Matrix x,Matrix y) {
		initplot2d()
		int l=asRowLength(x)
		((Plot2DPanel)currentplot).addBarPlot(name,asRow(x,l),asRow(y,l))
		return currentplot
	}
	
	static PlotPanel plot2d_cloud(String name,Matrix sample, int slices_x,int slices_y) {
		initplot2d()
		((Plot2DPanel)currentplot).addCloudPlot(name,sample.getRef(),slices_x, slices_y)
		return currentplot
	}
	
	static PlotPanel plot2d_histogram(String name,Matrix sample, int slices_x) {
		initplot2d()
		((Plot2DPanel)currentplot).addHistogramPlot(name,sample.getRef(),slices_x)
		return currentplot
	}
	
	static PlotPanel plot2d(String name,Matrix xy,String type="SCATTER") {
		if (type.equals("SCATTER"))
			return plot2d_scatter(name,xy)
		else if (type.equals("LINE"))
			return plot2d_line(name,xy)
		else if (type.equals("BAR"))
			return plot2d_bar(name,xy)
		else if (type.equals("STAIRCASE"))
			return plot2d_staircase(name,xy)
		else throw new IllegalArgumentException("Plot type "+type+" unknown")	
	}
	
	static PlotPanel plot2d(String name,Matrix x, Matrix y,String type="SCATTER") {
		if (type.equals("SCATTER"))
			return plot2d_scatter(name,x,y)
		else if (type.equals("LINE"))
			return plot2d_line(name,x,y)
		else if (type.equals("BAR"))
			return plot2d_bar(name,x,y)
		else if (type.equals("STAIRCASE"))
			return plot2d_staircase(name,x,y)
		else throw new IllegalArgumentException("Plot type "+type+" unknown")	
	}
	
	static PlotPanel plot2d(String name,Matrix sample,int slices_x,int slices_y,String type="CLOUD") {
		if (type.equals("CLOUD"))
			return plot2d_cloud(name,sample, slices_x, slices_y)
		else throw new IllegalArgumentException("Plot type "+type+" unknown")	
	}
	
	static PlotPanel plot2d(String name,Matrix sample,int slices_x,String type="HISTOGRAM") {
		if (type.equals("HISTOGRAM"))
			return plot2d_histogram(name,sample, slices_x)
		else throw new IllegalArgumentException("Plot type "+type+" unknown")	
	}
	
	//////////////////////
	// Common plot call //
	//////////////////////
	
	static PlotPanel plot(String name,Matrix m,String type="SCATTER") {
		if (type.equals("SCATTER")) {
			if (m.getColumnsNumber()==2)
				return plot2d(name,m,"SCATTER")
			else if (m.getColumnsNumber()==3)
				return plot3d(name,m,"SCATTER")
			else throw new IllegalArgumentException("SCATTER plot needs (n*3) or (n*2) data")
		} else if (type.equals("LINE")) {
			if (m.getColumnsNumber()==2)
				return plot2d(name,m,"LINE")
			else if (m.getColumnsNumber()==3)
				return plot3d(name,m,"LINE")
			else throw new IllegalArgumentException("LINE plot needs (n*3) or (n*2) data")	
		} else if (type.equals("BAR")) {
			if (m.getColumnsNumber()==2)
				return plot2d(name,m,"BAR")
			else if (m.getColumnsNumber()==3)
				return plot3d(name,m,"BAR")
			else throw new IllegalArgumentException("LINE plot needs (n*3) or (n*2) data")	
		} else if (type.equals("STAIRCASE")) {
			if (m.getColumnsNumber()==2)
				return plot2d(name,m,"STAIRCASE")
			else throw new IllegalArgumentException("STAIRCASE plot needs (n*2) data")	
		} else if (type.equals("GRID")) {
			return plot3d(name,m,"GRID")
		} else throw new IllegalArgumentException("Plot type "+type+" unknown")	
	}
	
	static PlotPanel plot(String name,Matrix x, Matrix y,String type="SCATTER") {
		return plot2d(name, x, y,type)
	}
	
	static PlotPanel plot(String name,Matrix x, Matrix y,Matrix z,String type="SCATTER") {
		return plot3d(name, x, y,z,type)
	}
	
	static PlotPanel plot(String name,Matrix m,int slices_x,String type="HISTOGRAM") {
		if (type.equals("HISTOGRAM")) {
			return plot2d(name,m,slices_x,"HISTOGRAM")
		} else throw new IllegalArgumentException("Plot type "+type+" unknown")	
	}
	
	static PlotPanel histogram(String name,Matrix m,int slices_x) {
		return plot2d(name,m,slices_x,"HISTOGRAM")
	}
	
	static PlotPanel plot(String name,Matrix m,int slices_x,int slices_y,String type="HISTOGRAM") {
		if (type.equals("HISTOGRAM")) {
			return plot3d(name,m,slices_x,slices_y,"HISTOGRAM")
		} else if (type.equals("GRID")) {
			return plot2d(name,m,slices_x,slices_y,"GRID")
		} else throw new IllegalArgumentException("Plot type "+type+" unknown")	
	}
	
	static PlotPanel histogram(String name,Matrix m,int slices_x,int slices_y) {
		return plot3d(name,m,slices_x,slices_y,"HISTOGRAM")
	}
	
	static PlotPanel plot(String name,Matrix m,int slices_x,int slices_y,int slices_z,String type="GRID") {
		 if (type.equals("GRID")) {
			 return plot3d(name,m,slices_x,slices_y,slices_z,"GRID")
		} else throw new IllegalArgumentException("Plot type "+type+" unknown")	
	}
	

}