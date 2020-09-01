package Tests;



import static org.junit.Assert.*;

import org.junit.Test;

import Controleur.Controleur;
import Modele.ModeleFractale;
import Modele.ModeleFractale.Type;
import Outils.ComplexNumber;
import javafx.scene.control.SplitPane.Divider;
import javafx.scene.image.WritableImage;

public class TestsFractales {

	@Test
	public final void testTailleImage() {
		ModeleFractale m = new ModeleFractale();
		Controleur controleur = new Controleur(m);
		WritableImage w = controleur.paintImage(m.getImageWidth(), m.getImageWidth());
		assertTrue("TAILLE DE l'IMAGE",w.getHeight()*w.getWidth() == m.getImageWidth()*m.getImageWidth());
	}
	
	@Test
	public final void testType() {
		ModeleFractale m = new ModeleFractale();
		m.setType(Type.MANDELBROT);
		assertTrue(m.getType() == Type.MANDELBROT);
		assertFalse(m.getType() == Type.JULIA);
		assertFalse(m.getType() == Type.NEWTON);
		
		m.setType(Type.JULIA);
		assertFalse(m.getType() == Type.MANDELBROT);
		assertTrue(m.getType() == Type.JULIA);
		assertFalse(m.getType() == Type.NEWTON);
		
		m.setType(Type.NEWTON);
		assertFalse(m.getType() == Type.MANDELBROT);
		assertFalse(m.getType() == Type.JULIA);
		assertTrue(m.getType() == Type.NEWTON);
		
	}
	
	@Test
	public final void testZoom(){
		ModeleFractale m = new ModeleFractale();
		m.setType(Type.MANDELBROT);
		
		assertTrue( m.distanceAuCentre(m.getMANDELBROT_RE_MIN(), m.getMANDELBROT_RE_MAX()) == 0.2);
		assertTrue( m.distanceAuCentre(m.getMANDELBROT_IM_MIN(), m.getMANDELBROT_IM_MAX()) == 0.12);
		m.zoomIn();
		m.zoomIn();
		
		assertTrue(m.getIterationMax() == 90);
		assertTrue(m.getNombreZooms() == 2);
		assertTrue(m.getZoom_x() == 350.30864197530866);
		assertTrue(m.getZoom_y() == 411.5226337448559);
		
	}
	
	@Test
	public final void testRestart(){
		ModeleFractale m = new ModeleFractale();
		m.setType(Type.MANDELBROT);
		
		m.zoomIn();
		m.restart();
		
		assertTrue(m.getMANDELBROT_RE_MIN() == -2.5);		
		assertTrue(m.getMANDELBROT_RE_MAX() ==  1.5);
		assertTrue(m.getMANDELBROT_IM_MIN() == -1.2);
		assertTrue(m.getMANDELBROT_IM_MAX() == 1.2);
		
		m.setType(Type.JULIA);
		
		m.zoomIn();
		m.restart();
		
		assertTrue(m.getJULIA_RE_MIN() == -1.5);		
		assertTrue(m.getJULIA_RE_MAX() ==  1.5);
		assertTrue(m.getJULIA_IM_MIN() == -1.5);
		assertTrue(m.getJULIA_IM_MAX() == 1.5);
		
		m.setType(Type.NEWTON);
		m.zoomIn();
		m.restart();
		assertTrue(m.getZoom_x() == m.getImageWidth()/5 );
		assertTrue(m.getZoom_y() == m.getImageHeight()/5 );
	}

	

}

