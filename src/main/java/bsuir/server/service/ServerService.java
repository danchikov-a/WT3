package bsuir.server.service;

import bsuir.server.entity.criteria.Criteria;
import bsuir.server.entity.info.Info;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;

public interface ServerService {
	List<Info> getAll();
	Info getUser(Criteria criteria);
	Info getStudent(Criteria criteria);
	boolean registerStudent(String name, String newName, String averageScore);
	boolean addStudent(String name, String averageScore) throws ParserConfigurationException, TransformerException, SAXException, IOException;
	boolean addUser(String name, String password, String allowance) throws IOException, SAXException, ParserConfigurationException, TransformerException;
}
