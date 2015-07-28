package co.com.pulxar.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import co.com.pulxar.entities.Grabacion;

/**
 * Servlet implementation class GrabacionLlamada
 */
@WebServlet("/GrabacionLlamada")
public class GrabacionLlamada extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Resource UserTransaction ut;
	@PersistenceContext(unitName="AsteriskTools") protected EntityManager em;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GrabacionLlamada() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Date date = Calendar.getInstance().getTime();
		Timestamp timestamp = new Timestamp(date.getTime());
		Grabacion g = new Grabacion();
		
		String envio = request.getParameter("valor");
		String uniqueid = request.getParameter("uid");
		
		String[] parametros =envio.split("-");
		
		g.setNumorigen(parametros[1]);
		g.setNumdestino(parametros[2]);
		g.setCanal(parametros[3]);
		g.setDestinollamada(parametros[4]);
		g.setNodo(parametros[5]);
		g.setArchivo(envio);
		g.setFecha(timestamp);
		g.setUniqueid(uniqueid);
		
		try {
			ut.begin();
			em.persist(g);
			ut.commit();
		} catch (NotSupportedException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (RollbackException e) {
			e.printStackTrace();
		} catch (HeuristicMixedException e) {
			e.printStackTrace();
		} catch (HeuristicRollbackException e) {
			e.printStackTrace();
		}
		
	}

}
