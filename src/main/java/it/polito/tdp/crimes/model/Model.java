package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	private Graph<String, DefaultWeightedEdge>grafo;
	private EventsDao dao;
	private List<String> best;//lista che contiene i vertici del cammino migliore
	
	public Model() {
		dao = new EventsDao();
	}
	
	public void creaGrafo(String categoria, int mese) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiunta vertici al grafo
		Graphs.addAllVertices(this.grafo, dao.getVertici(categoria, mese));
		
		//aggiunta archi al grafo
		
		for(Adiacenza a : dao.getArchi(categoria, mese)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getV1(),a.getV2(),a.getPeso());
		}
		
		//TODO riempire la tendina degli archi
		
		System.out.println("Grafo Creato");
		System.out.println("# VERTICI: "+this.grafo.vertexSet().size());
		System.out.println("# ARCHI: "+this.grafo.edgeSet().size());
	}
	
	public List<Adiacenza>getArchiMaggiorPesoMedio(){
		//scorro gli archi del grafo e calcolo il peso medio
		double pesoTot = 0.0;
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			pesoTot += this.grafo.getEdgeWeight(e);
		}
		double avg = pesoTot / this.grafo.edgeSet().size();
		System.out.println("PESO MEDIO : "+avg);
		
		//scorro tutti gli archi e prendo quelli con peso > avg
		List<Adiacenza> result = new ArrayList<>();
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(e)>avg) {
				result.add(new Adiacenza(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e),(int)this.grafo.getEdgeWeight(e)));
			}
		}
		return result;
	}
	
	public List<String>calcolaPercorso(String sorgente, String destinazione){
		best = new LinkedList<>();
		List<String>parziale = new LinkedList<>();
		parziale.add(sorgente);//aggiunta del primo step
		cerca(parziale,destinazione);
		return best;
	}

	private void cerca(List<String> parziale, String destinazione) {
		//condizione di terminazione
		if(parziale.get(parziale.size()-1).equals(destinazione)) {
			// è la soluzione migliore?
			if(parziale.size()>best.size()) {
				best = new LinkedList<>(parziale);
			}
			return;
		}
		
		//scooro i vicini dell'ultimo inserito e provo le varie "strade"
		for(String v: Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			if(!parziale.contains(v)) {
			parziale.add(v);//aggiungo i vertici a parziale
			cerca(parziale,destinazione);//attuo la ricorsione
			parziale.remove(parziale.size()-1);//faccio backtracking ed esploro gli altri vertici
			}
		}	
	}
	
	
	
}
