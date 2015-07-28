package co.com.pulxar.backbean;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

public class DashboardBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	//select DATE(fecha) as FECHAS , count(*) from grabacion group by DATE(fecha) order by FECHAS desc limit 30;
	//http://stackoverflow.com/questions/17202334/jpa-entity-manager-select-many-columns-and-get-result-list-custom-objects
	//select dst, count(*) from cdr where calldate > '04/01/2015 00:00:00' and dst like '1___' group by dst;
	
	private Boolean isEmptyGrabacionAgenteDiarioSaliente = true;
	private Boolean isEmptyGrabacionAgenteDiarioEntrante = true;
	private Boolean isEmptyGrabacionAgenteMensualSaliente = true;
	private Boolean isEmptyGrabacionAgenteMensualEntrante = true;
	
	private LineChartModel GrabacionFecha;
	private HorizontalBarChartModel GrabacionAgenteDiarioSaliente;
	private HorizontalBarChartModel GrabacionAgenteDiarioEntrante;
	private BarChartModel GrabacionAgenteMensualSaliente;
	private BarChartModel GrabacionAgenteMensualEntrante;
	@PersistenceContext	EntityManager em;


	public Boolean getIsEmptyGrabacionAgenteDiarioSaliente() {
		return isEmptyGrabacionAgenteDiarioSaliente;
	}
	
	public Boolean getIsEmptyGrabacionAgenteDiarioEntrante() {
		return isEmptyGrabacionAgenteDiarioEntrante;
	}

	public Boolean getIsEmptyGrabacionAgenteMensualSaliente() {
		return isEmptyGrabacionAgenteMensualSaliente;
	}

	public Boolean getIsEmptyGrabacionAgenteMensualEntrante() {
		return isEmptyGrabacionAgenteMensualEntrante;
	}

	public LineChartModel getGrabacionFecha() {
		return GrabacionFecha;
	}

	public HorizontalBarChartModel getGrabacionAgenteDiarioSaliente() {
		return GrabacionAgenteDiarioSaliente;
	}


	public HorizontalBarChartModel getGrabacionAgenteDiarioEntrante() {
		return GrabacionAgenteDiarioEntrante;
	}


	public BarChartModel getGrabacionAgenteMensualSaliente() {
		return GrabacionAgenteMensualSaliente;
	}


	public BarChartModel getGrabacionAgenteMensualEntrante() {
		return GrabacionAgenteMensualEntrante;
	}


	@PostConstruct
	public void init() {
		datosGrabacionesDia();
		datosllamadaAgenteDiarioEntrante();
		datosllamadaAgenteDiarioSaliente();
		datosllamadaAgenteMensualEntrante();
		datosllamadaAgenteMensualSaliente();
	}
	
	private void datosGrabacionesDia() {
		
    	DateAxis axis = new DateAxis("Fechas");
    	LineChartSeries series1 = new LineChartSeries();
    	List<Object[]> resultList;

    	Query query = em.createNativeQuery(
				  "SELECT DATE(g.fecha) as FECHAS , count(*) "
				+ "FROM grabacion g "
				+ "GROUP BY DATE(g.fecha) "
				+ "ORDER BY FECHAS desc").setMaxResults(30);
		resultList = query.getResultList();
		for(Object[] obj : resultList){
			series1.set( obj[0].toString(), (BigInteger) obj[1]);
		}
		
		GrabacionFecha = new LineChartModel();
		GrabacionFecha.setTitle("Total grabaciones por dia");
        GrabacionFecha.getAxis(AxisType.Y).setLabel("Grabaciones");
        GrabacionFecha.getAxes().put(AxisType.X, axis);
        GrabacionFecha.addSeries(series1);       
    }
	
	private void datosllamadaAgenteDiarioSaliente() {
		
		GrabacionAgenteDiarioSaliente = new HorizontalBarChartModel();
		GrabacionAgenteDiarioSaliente.setTitle("Grabaciones de los agentes por dia (Salida)");
		
		Calendar fecha = Calendar.getInstance();
		fecha.set(Calendar.HOUR_OF_DAY,0);
		fecha.set(Calendar.MINUTE, 0);
		fecha.set(Calendar.SECOND, 0);
		
		ChartSeries llamadasSalientes = new ChartSeries();
		llamadasSalientes.setLabel("Salientes");
		
		List<Object[]> resultList;
		
		Query query = em.createNativeQuery(
				"SELECT numorigen, count(*) "
				+ "FROM grabacion "
				+ "WHERE fecha > :feBuscar "
				+ "AND numorigen like '1___' "
				+ "GROUP BY numorigen "
				+ "ORDER BY numorigen").setParameter("feBuscar",fecha.getTime());
		List<Object[]> salientes;
		salientes = query.getResultList();
		
		for(Object[] obj : salientes){
			String temp = obj[0].toString();
			if(!temp.equals("1000")){
				llamadasSalientes.set( obj[0].toString(), (BigInteger) obj[1]);
				isEmptyGrabacionAgenteDiarioSaliente=false;
			}
		}
		
		GrabacionAgenteDiarioSaliente.addSeries(llamadasSalientes);
		GrabacionAgenteDiarioSaliente.setLegendPosition("e");
		
	}

	private void datosllamadaAgenteDiarioEntrante() {
		
		GrabacionAgenteDiarioEntrante = new HorizontalBarChartModel();
		GrabacionAgenteDiarioEntrante.setTitle("Grabaciones de los agentes por dia (Entrada)");
		
		Calendar fecha = Calendar.getInstance();
		fecha.set(Calendar.HOUR_OF_DAY,0);
		fecha.set(Calendar.MINUTE, 0);
		fecha.set(Calendar.SECOND, 0);
		
		ChartSeries llamadasEntrantes = new ChartSeries();
		llamadasEntrantes.setLabel("Entrantes");
		
		Query query2 = em.createNativeQuery(
				"SELECT numdestino, count(*) "
				+ "FROM grabacion "
				+ "WHERE fecha > :feBuscar "
				+ "AND numdestino like '1___' "
				+ "GROUP BY numdestino "
				+ "ORDER BY numdestino").setParameter("feBuscar",fecha.getTime());
		List<Object[]> entrantes;
		entrantes = query2.getResultList();
		for(Object[] obj : entrantes){
			String temp = obj[0].toString();
			if(!temp.equals("1000")){
				llamadasEntrantes.set( obj[0].toString(), (BigInteger) obj[1]);
				isEmptyGrabacionAgenteDiarioEntrante=false;
			}
			
		}
		GrabacionAgenteDiarioEntrante.addSeries(llamadasEntrantes);
		GrabacionAgenteDiarioEntrante.setLegendPosition("e");
		
	}
	
	private void datosllamadaAgenteMensualSaliente() {
		
		GrabacionAgenteMensualSaliente = new BarChartModel();
		GrabacionAgenteMensualSaliente.setTitle("Grabaciones de los agentes por mes (Salida)");
		
		ChartSeries llamadasSalientes = new ChartSeries();
		llamadasSalientes.setLabel("Salientes");
		
		Calendar fecha = Calendar.getInstance();
		fecha.set(Calendar.DAY_OF_MONTH, 1);
		fecha.set(Calendar.HOUR_OF_DAY,0);
		fecha.set(Calendar.MINUTE, 0);
		fecha.set(Calendar.SECOND, 0);
		
		Query query = em.createNativeQuery(
				"SELECT numorigen, count(*) "
				+ "FROM grabacion "
				+ "WHERE fecha > :feBuscar "
				+ "AND numorigen like '1___' "
				+ "GROUP BY numorigen "
				+ "ORDER BY numorigen").setParameter("feBuscar",fecha.getTime());
		List<Object[]> salientes;
		salientes = query.getResultList();
		for(Object[] obj : salientes){
			String temp = obj[0].toString();
			if(!temp.equals("1000")){
				llamadasSalientes.set( obj[0].toString(), (BigInteger) obj[1]);
				isEmptyGrabacionAgenteMensualSaliente = false;
			}			
		}
		
		GrabacionAgenteMensualSaliente.addSeries(llamadasSalientes);
		GrabacionAgenteMensualSaliente.setLegendPosition("e");
	}
	
	private void datosllamadaAgenteMensualEntrante() {
		
		GrabacionAgenteMensualEntrante = new BarChartModel();
		GrabacionAgenteMensualEntrante.setTitle("Grabaciones de los agentes por mes (Entrada)");
		
		ChartSeries llamadasEntrantes = new ChartSeries();
		llamadasEntrantes.setLabel("Entrantes");
		
		Calendar fecha = Calendar.getInstance();
		fecha.set(Calendar.DAY_OF_MONTH, 1);
		fecha.set(Calendar.HOUR_OF_DAY,0);
		fecha.set(Calendar.MINUTE, 0);
		fecha.set(Calendar.SECOND, 0);
		
		Query query2 = em.createNativeQuery(
				"SELECT numdestino, count(*) "
				+ "FROM grabacion "
				+ "WHERE fecha > :feBuscar "
				+ "AND numdestino like '1___' "
				+ "GROUP BY numdestino "
				+ "ORDER BY numdestino").setParameter("feBuscar",fecha.getTime());
		List<Object[]> entrantes;
		entrantes = query2.getResultList();
		for(Object[] obj : entrantes){
			String temp = obj[0].toString();
			if(!temp.equals("1000")){
				llamadasEntrantes.set( obj[0].toString(), (BigInteger) obj[1]);
				isEmptyGrabacionAgenteMensualEntrante = false;
			}
		}
		GrabacionAgenteMensualEntrante.addSeries(llamadasEntrantes);
		GrabacionAgenteMensualEntrante.setLegendPosition("e");
	}
}
