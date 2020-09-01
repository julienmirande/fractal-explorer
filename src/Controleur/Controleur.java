package Controleur;

import Modele.ModeleFractale;
import Modele.ModeleFractale.Type;
import Outils.ComplexNumber;
import Outils.MyThreads;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import java.awt.Color;

public class Controleur {
	ModeleFractale modele;

	public Controleur(ModeleFractale modele) {
		this.modele = modele;
	}

	public void zoomIn() {
		modele.zoomIn();
	}

	public void zoomOut() {
		if (modele.getNombreZooms() > 0) {
			modele.zoomOut();
		}
	}

	public void setType(ModeleFractale.Type type) {
		this.modele.setType(type);
	}

	public void decalerCanvas(double newMouseX, double newMouseY) {
		this.modele.decaler(newMouseX, newMouseY);
	}

	public WritableImage paintImage(int WIDTH, int HEIGHT) {

		// On commence par créer notre writable image, puis son pixelwriter
		WritableImage writableImage = new WritableImage((int) WIDTH, (int) HEIGHT);
		// PixelWriter px = writableImage.getPixelWriter();

		if (modele.getType() == Type.MANDELBROT || modele.getType() == Type.JULIA) {
			MyThreads m1 = new MyThreads(modele, 0, 0, WIDTH / 4, HEIGHT / 2, writableImage); // 1,0,0,0
			MyThreads m2 = new MyThreads(modele, WIDTH / 4, 0, WIDTH / 4 + 1, HEIGHT / 2, writableImage); // 0,1,0,0
			MyThreads m3 = new MyThreads(modele, WIDTH / 2, 0, WIDTH / 4, HEIGHT / 2, writableImage); // 0,0,1,0
			MyThreads m4 = new MyThreads(modele, WIDTH / 2 + WIDTH / 4, 0, WIDTH / 4, HEIGHT / 2, writableImage); // 0,0,0,1
			MyThreads m5 = new MyThreads(modele, 0, HEIGHT / 2, WIDTH / 4, HEIGHT / 2, writableImage);
			MyThreads m6 = new MyThreads(modele, WIDTH / 4, HEIGHT / 2, WIDTH / 4 + 1, HEIGHT / 2, writableImage);
			MyThreads m7 = new MyThreads(modele, WIDTH / 2, HEIGHT / 2, WIDTH / 4, HEIGHT / 2, writableImage);
			MyThreads m8 = new MyThreads(modele, WIDTH / 2 + WIDTH / 4, HEIGHT / 2, WIDTH / 4, HEIGHT / 2,writableImage);

			m1.start();
			m2.start();
			m3.start();
			m4.start();
			m5.start();
			m6.start();
			m7.start();
			m8.start();

			try {
				m1.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("t1");
				// e.printStackTrace();
			}
			try {
				m2.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("t2");
				// e.printStackTrace();

			}
			try {
				m3.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				m4.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				m5.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				m6.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				m7.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				m8.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} else {
			writableImage = imageNewton(WIDTH, HEIGHT, modele.getIterationMax());
		}

		return writableImage;

	}



	public WritableImage imageNewton(double WIDTH, double HEIGHT, int iterationsMax) {

		// La encore, on commence par créer notre writable image, puis son
		// pixelwriter
		WritableImage writableImage = new WritableImage((int) WIDTH, (int) HEIGHT);
		PixelWriter px = writableImage.getPixelWriter();
		// On initialise la couleur de base, la saturation et la luminosité
		float hue = 0f;
		float saturation = 1f;
		float brightness = 0.95f;

		for (int X = 0; X < WIDTH; X++) {
			for (int Y = 0; Y < HEIGHT; Y++) {

				// C'est dans cette fonction que la classe ComplexNumber prend
				// tout son intérêt
				// On commence par initialiser le nombre complexe a en fonction
				// de X et Y, ainsi que de la
				// taille de l'image et du zoom
				ComplexNumber a = new ComplexNumber(((X - WIDTH / modele.getDecalage_X() / 2) / modele.getZoom_x()),
						((Y - HEIGHT / modele.getDecalage_Y() / 2) / modele.getZoom_y()));
				int iteration = 0;

				// On commence maintenant le calcul de la convergence
				for (; iteration < iterationsMax; iteration++) {
					ComplexNumber f = ComplexNumber.pow(a, 5);
					f.subtract(new ComplexNumber(1, 0));
					ComplexNumber df = ComplexNumber.pow(a, 4);
					df.multiply(new ComplexNumber(5, 0));
					a.subtract(ComplexNumber.divide(f, df));
					ComplexNumber b = new ComplexNumber();
					b = a;
					b = ComplexNumber.pow(b, 5);
					b.subtract(new ComplexNumber(1, 0));
					if (Math.abs(b.mod()) < ModeleFractale.getEPSILON())
						break;
				}

				// Puis on détermine la couleur en fonction de l'itération
				hue = (float) iteration / ModeleFractale.getDefaulIterationMax();
				px.setArgb(X, Y, Color.getHSBColor(hue, saturation, brightness).getRGB());
			}
		}
		return writableImage;
	}

	// remettre l'état initial
	public void restart() {
		modele.restart();

	}

}
