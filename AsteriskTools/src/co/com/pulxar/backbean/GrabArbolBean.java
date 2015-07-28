package co.com.pulxar.backbean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import co.com.pulxar.entities.Grabacion;

@ManagedBean
public class GrabArbolBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TreeNode root;
	private TreeNode nodoSelecionado;
	private List<Grabacion> grabaciones;
	@PersistenceContext	EntityManager em;
	
	
	public GrabArbolBean() {
		root = new DefaultTreeNode("root", null);
	}
	
	public TreeNode getRoot() {
		return root;
	}
	
	public void setRoot(TreeNode root) {
		this.root = root;
	}
	
	public TreeNode getNodoSelecionado() {
		return nodoSelecionado;
	}

	public void setNodoSelecionado(TreeNode nodoSelecionado) {
		this.nodoSelecionado = nodoSelecionado;
	}

	public List<Grabacion> getGrabaciones() {
		return grabaciones;
	}

	public void setGrabaciones(List<Grabacion> grabaciones) {
		this.grabaciones = grabaciones;
	}

	@PostConstruct
	public void obtenerArbolGrabaciones() {
		Query q0 = em.createNativeQuery("select nodo from grabacion group by nodo order by nodo asc");
		List<String> nodos = q0.getResultList();
		for (String nodo : nodos) {
			TreeNode temp0 = new DefaultTreeNode(nodo, root);
			Query q1 = em.createNativeQuery("select EXTRACT(year from fecha) as date from Grabacion where nodo=:nodo group by date order by date desc");
			q1.setParameter("nodo", temp0.getData());
			List<Double> anos = q1.getResultList();
			for(Double ano:anos){
				TreeNode temp1 = new DefaultTreeNode(ano.intValue(), temp0);
				Query q2 = em.createNativeQuery("select EXTRACT(month from fecha) as date from Grabacion where nodo=:nodo and EXTRACT(year from fecha)=:ano group by date order by date desc");
				q2.setParameter("nodo", temp0.getData());
				q2.setParameter("ano", temp1.getData());
				List<Double> meses = q2.getResultList();
				for (Double mes : meses) {
					TreeNode temp2 = new DefaultTreeNode(mes.intValue(),temp1);
					Query q3 = em.createNativeQuery("select EXTRACT(day from fecha) as date from Grabacion where nodo=:nodo and EXTRACT(year from fecha)=:ano and EXTRACT(month from fecha)=:mes group by date order by date desc");
					q3.setParameter("nodo", temp0.getData());
					q3.setParameter("ano", temp1.getData());
					q3.setParameter("mes", temp2.getData());
					List<Double> dias = q3.getResultList();
					for (Double dia : dias) {
						TreeNode temp3 = new DefaultTreeNode(dia.intValue(),temp2);
					}
				}
			}
		}
	}
	
	public void onNodeSelect(NodeSelectEvent event) {
		Integer dia = (Integer) event.getTreeNode().getData();
		if(dia.toString().length() == 1 || dia.toString().length() == 2){
			Integer mes = (Integer) event.getTreeNode().getParent().getData();
			if(mes.toString().length() == 1 || mes.toString().length() == 2){
				Integer ano = (Integer) event.getTreeNode().getParent().getParent().getData();
				if(ano.toString().length() == 4 ){
					String nodo = (String) event.getTreeNode().getParent().getParent().getParent().getData();
					Calendar  fechaTemp = Calendar.getInstance();
					fechaTemp.set(ano, mes-1, dia,0,0,0);
					Date fechaIncio = fechaTemp.getTime();
					fechaTemp = Calendar.getInstance();
					fechaTemp.set(ano, mes-1, dia,23,59,59);
					Date fechaFinal = fechaTemp.getTime();
					Query q = em.createQuery("select g from Grabacion g where nodo = :nodo and fecha between :fechaInicio and :fechaFinal");
					q.setParameter("fechaInicio", fechaIncio);
					q.setParameter("fechaFinal", fechaFinal);
					q.setParameter("nodo", nodo);
					grabaciones = q.getResultList();
				}
			}
		}
    }  
}