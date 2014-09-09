package com.me.mst;

import java.util.Hashtable;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;



public class Grafo {
	public static BitmapFont bf = new BitmapFont(Gdx.files.internal("arial15.fnt"), Gdx.files.internal("arial15.png"), false);
	public static final byte raio = 10;
	public static Grafo grafoentrada;
	public Hashtable<Integer, Vector2> vertices; //número do vértice / posição (X,Y) no espaço de representação
	public Hashtable<Vector2, Integer> arestas; //um vértice se liga à um array de outros vertices
	public Vector3 rgb;
	
	
	private float getGW(){
		float aux = (float) (Math.random()*(Gdx.graphics.getWidth() - raio));
		aux = (aux < raio) ? raio : aux;
		
		//pros vertices não estarem muito perto um do outro
		boolean proximo = false;
		Set<Integer> keys = this.vertices.keySet();
        for(Integer key: keys){
        	if (Math.abs(vertices.get(key).x - aux) < raio*2) proximo = true;
        }
        if (proximo) aux = getGW();
		return aux;
	}
	private float getGH(){
		float aux = (float) (Math.random()*(Gdx.graphics.getHeight() - raio));
		aux = (aux < raio) ? raio : aux;
		
		//pros vertices não estarem muito perto
		boolean proximo = false;
		Set<Integer> keys = this.vertices.keySet();
        for(Integer key: keys){
        	if (Math.abs(vertices.get(key).y - aux) < raio*2) proximo = true;
        }
        if (proximo) aux = getGH();
		return aux;
	}
	
	public Grafo(int verticeid){
		vertices = new Hashtable<Integer, Vector2>();
		arestas = new Hashtable<Vector2, Integer>();
		vertices.put(verticeid, new Vector2((float) (getGW()), 
				(float)(getGH())));
		rgb = new Vector3((float)Math.random(),(float)Math.random(), (float)Math.random());
		while (rgb.x < 0.2f || rgb.y < 0.2f || rgb.z < 0.2f){//pra não ficarem com cores muito escuras
			rgb = new Vector3((float)Math.random(),(float)Math.random(), (float)Math.random());
		}
	}
	public void sincronizarVertice(int verticeid){
		this.vertices.put(verticeid, grafoentrada.vertices.get(verticeid));
	}
	
	public void addVI(int id){//adicionar vértices inicialmente, grafo inicial
		vertices.put(id, new Vector2((float) (getGW()), 
				(float)(getGH())));
	}
	public void addAI(int origem, int destino, int custo){//adicionar artestas inicialmente, grafo inicial
		arestas.put(new Vector2(origem, destino), custo);
	}
	public void adicionarVertice(int id){
		if (!vertices.contains(id)) vertices.put(id, grafoentrada.vertices.get(id));
	}
	public void adicionarAresta(int origem, int destino){
		//sempre que adicionar uma nova aresta, é necessário que os dois vértices já estejam no grafo
		this.adicionarVertice((int) origem);
		this.adicionarVertice((int) destino);
		//adicionar aresta
		boolean achou = false;
        Set<Vector2> keys = this.arestas.keySet();
        for(Vector2 key: keys){
        	if (key.x == origem && key.y == destino) achou = true;
        }
		if (!achou) {
	        keys = grafoentrada.arestas.keySet();
	        int custo = -1;
	        for(Vector2 key: keys){
	        	if (key.equals(new Vector2(origem, destino))) custo = grafoentrada.arestas.get(key);
	        }
			this.arestas.put(new Vector2(origem, destino), custo);
		}
	}
	public void juntar(Grafo grafo){
		if (this == grafo) return;
		Set<Integer> keys = grafo.vertices.keySet();
        for(Integer key: keys){
        	this.adicionarVertice(key);
        }
		
        Set<Vector2> keys2 = grafo.arestas.keySet();
        for(Vector2 key: keys2){
        	this.adicionarAresta((int)key.x, (int)key.y);
        }
	}
	
	public boolean contemVerticeComum(Grafo grafo){
		Set<Integer> keys = grafo.vertices.keySet();
		System.out.println(this.vertices.keySet() + "lol" +keys);
        for(Integer key: keys){
        	//this.adicionarVertice(key);
        	if (this.vertices.containsKey(key)) {
        		return true;
        	}
        }
        /*keys = this.vertices.keySet();
        for(Integer key: keys){
        	if (grafo.vertices.containsKey(key)) return true;
        }*/
		return false;
	}
	public boolean contemArestaComum(Grafo grafo){
		if (this == grafo) return true;
		Set<Vector2> keys = this.arestas.keySet();
		Set<Vector2> keys2 = grafo.arestas.keySet();
        for(Vector2 key: keys){
            for(Vector2 key2: keys2){
            	if (key.x == key2.x && key.y == key2.y) return true;
            	else if (key.x == key2.y && key.y == key2.x) return true;
            }
        }
        return false;
	}
	
	public void desenhar(ShapeRenderer shapeRenderer){
		
		shapeRenderer.begin(ShapeType.FilledCircle);
		shapeRenderer.setColor(rgb.x, rgb.y, rgb.z, 1);
		//desenhando vertices
		Set<Integer> keys = this.vertices.keySet();
        for(Integer key: keys){
        	shapeRenderer.filledCircle(this.vertices.get(key).x, this.vertices.get(key).y, raio);
        }
        
        //desenhando arestas
        shapeRenderer.end();
        shapeRenderer.begin(ShapeType.Line);
		Set<Vector2> keys2 = this.arestas.keySet();
        for(Vector2 key: keys2){
        	float x1 = this.vertices.get((int)key.x).x,
        			y1 = this.vertices.get((int)key.x).y,
        			x2 = this.vertices.get((int)key.y).x,
        			y2 = this.vertices.get((int)key.y).y;
        	shapeRenderer.line(x1, y1, x2, y2);
        	
        }
		shapeRenderer.end();

	}
	public void desenhar(SpriteBatch batch){
		//desenhar numero vertice
		bf.setColor(Color.BLACK);
		Set<Integer> keys = this.vertices.keySet();
        for(Integer key: keys){
        	bf.draw(batch, key.toString(), this.vertices.get(key).x - raio/2, this.vertices.get(key).y + raio/2 + 3);	
        }
		
		//desenhar custo aresta
        
		Set<Vector2> keys2 = this.arestas.keySet();
        for(Vector2 key: keys2){
        	float x1 = this.vertices.get((int)key.x).x,
        			y1 = this.vertices.get((int)key.x).y,
        			x2 = this.vertices.get((int)key.y).x,
        			y2 = this.vertices.get((int)key.y).y;
        	//desenhar cargas
        	float xa = (x1 < x2) ? x1 : x2;
        	float ya = (y1 < y2) ? y1 : y2;
        	bf.setColor(Color.BLACK);
        	bf.draw(batch, this.arestas.get(key).toString(), xa + Math.abs(x2 - x1)/2 + 1, ya + Math.abs(y2 - y1)/2 - 1);	
        	bf.draw(batch, this.arestas.get(key).toString(), xa + Math.abs(x2 - x1)/2 - 1, ya + Math.abs(y2 - y1)/2 + 1);	
        	bf.setColor(Color.toFloatBits(rgb.x, rgb.y, rgb.z, 1));
        	bf.draw(batch, this.arestas.get(key).toString(), xa + Math.abs(x2 - x1)/2, ya + Math.abs(y2 - y1)/2);	
        }
	}
	
	public static int getCustoAresta(float origem, float destino){
		if (origem == destino) return -1;
		Set<Vector2> keys = grafoentrada.arestas.keySet();
        for(Vector2 key: keys){
        	int custo1 = -1, custo2 = -1;
        	/*if (key.equals(new Vector2(origem, destino))) custo1 = grafoentrada.arestas.get(key);
        	else if (key.equals(new Vector2(destino, origem))) custo2 = grafoentrada.arestas.get(key);
        	*/
        	if (((int)key.x == (int)origem) && ((int)key.y == (int)destino)) custo1 = grafoentrada.arestas.get(key);
        	else if (((int)key.y == (int)origem) && ((int)key.x == (int)destino)) custo2 = grafoentrada.arestas.get(key);        	
        	if (custo1 != -1 && custo2 != -1){
            	if (custo1 < custo2) return custo1;
            	else return custo2;
        	}else if (custo1 != -1) return custo1;
        	else if (custo2 != -1) return custo2;
        }
        return -1;
	}
}
