package Outils;

import java.awt.Color;

import Modele.ModeleFractale;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class MyThreads extends Thread {
	private ModeleFractale modele;
	private WritableImage writableImage;
	private int x, y, xLenght;

	private int yLenght;

	public MyThreads(ModeleFractale modele, int x, int y, int xLenght, int yLenght, WritableImage wi) {
		this.modele = modele;
		this.writableImage = wi;
		this.x = x;
		this.y = y;
		this.xLenght = xLenght;
		this.yLenght = yLenght;
	}

	@Override
	public void run() {
		PixelWriter px = writableImage.getPixelWriter();
		double convergenceValue = 0.0;
		
		for (int xR = x; xR < x + xLenght; xR++) {
			for (int yR = y; yR < y + yLenght; yR++) {

				double c_r;
				double c_i;

				// En fonction du type de la fractale, sa convergence se calcule
				// différemment, dans le cas
				// de Newton, le détail de son calcul sera expliqué dans sa
				// fonction dédiée
				switch (modele.getType()) {
				case MANDELBROT:
					c_r = xR / modele.getZoom_x() + modele.getMANDELBROT_RE_MIN();
					c_i = yR / modele.getZoom_y() + modele.getMANDELBROT_IM_MIN();
					convergenceValue = checkConvergence(c_i, c_r, modele.getZ(), modele.getZi(),
							modele.getIterationMax());
					break;
				case JULIA:
					c_r = xR / modele.getZoom_x() + modele.getJULIA_RE_MIN();
					c_i = yR / modele.getZoom_y() + modele.getJULIA_IM_MIN();
					convergenceValue = checkConvergence(modele.getZ(), modele.getZi(), c_i, c_r,
							modele.getIterationMax());
					break;
				default:
					break;
				}

				// on calcul l'intensité de la couleur
				double t1 = (double) convergenceValue / ModeleFractale.getDefaulIterationMax();
				double c1 = Math.min(360 * 2 * t1, 360) / 360;

				// si la valeur de convergence est inférieure au nombre
				// d'itérations maximum, on colorie le pixel en fonction de
				// cette valeur
				if (convergenceValue != modele.getIterationMax()) {
					px.setArgb(xR, yR,
							Color.getHSBColor((float) (convergenceValue / 8), (float) c1, (float) 1).getRGB());

				} else {
					px.setArgb(xR, yR, Color.getHSBColor((float) 240.0, (float) 1, (float) 0.0).getRGB());// Convergence
					
				}
				
			}

		}

	}

	private int checkConvergence(double ci, double c, double z, double zi, int convergenceSteps) {
		for (int i = 0; i < convergenceSteps; i++) {
			double ziT = 2 * (z * zi);
			double zT = z * z - (zi * zi);
			z = zT + c;
			zi = ziT + ci;

			if (z * z + zi * zi >= 4.0) {
				return i;
			}
		}

		return convergenceSteps;
	}
	

}
