package bsuir.server.dao;

import bsuir.server.entity.criteria.Criteria;
import bsuir.server.entity.info.Info;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;

public interface ApplianceDAO {
	List<Info> getAll(String path, String type);
	List<Info> get(Criteria criteria, String path, String type);
	boolean add(List<String[]> parameters, String path, String type) throws ParserConfigurationException, IOException, SAXException, TransformerException;
	boolean edit(String name, List<String[]> parameters, String path);
}
