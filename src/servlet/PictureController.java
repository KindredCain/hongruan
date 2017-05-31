package servlet;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import hibernate.HibernateUtil;
import model.Label;
import model.Picture;
import model.Worker;

@Controller
public class PictureController {

	/*----------上传图片------------*/
	@RequestMapping("uppicture.do")
	public String AddPicture(@RequestParam("upimg") MultipartFile[] files, HttpServletRequest request) throws IllegalStateException, IOException {  
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		if(files!=null&&files.length>0){ 
			try {
				session.beginTransaction();
				for(int i = 0;i<files.length;i++){ 
					MultipartFile file = files[i];  
					if (!file.isEmpty()) {
						String myFileName = file.getOriginalFilename();
						String Owner = (String) request.getSession().getAttribute("ID");
//						File uploadFile = new File("G:" + File.separator + "image" + File.separator + Owner);
//						String path = "G:" + File.separator + "image" + File.separator + Owner + File.separator + myFileName;
//						String srcpath = "G:" + File.separator + "image" + File.separator + Owner + File.separator + myFileName; 
						File uploadFile = new File("D:" + File.separator + "Tomcat 8.0" + File.separator + "webapps" + File.separator + "hongruan" + File.separator + "image" + File.separator + Owner);
						String path = "D:" + File.separator + "Tomcat 8.0" + File.separator + "webapps" + File.separator + "hongruan" + File.separator + "image" + File.separator + Owner + File.separator + myFileName;
						String srcpath = "." + File.separator + "image" + File.separator + Owner + File.separator + myFileName;
						if(!uploadFile.exists()){
							uploadFile.mkdir();
						}
						File localFile = new File(path);  
						file.transferTo(localFile);
						Picture picture = new Picture();
						picture.setPadd(srcpath);
						picture.setPname(myFileName);
						session.save(picture);
					}
				}
				session.getTransaction().commit();
			} catch (Exception ex) {
				request.getSession().setAttribute("errormsg", ex.getMessage());
				return "login";
			}  
		}   
		return "close";  
	}  

	/*----------修改图片期限--------*/
	@RequestMapping("moddead.do")
	public String ModDead(@RequestParam(value="pid")String[] pid, @RequestParam(value="pmodpeo")String pmodpeo, @RequestParam(value="year")String year, @RequestParam(value="month")String month, @RequestParam(value="day")String day, HttpServletRequest request) {
		try {
			String pdeadline = year + "-" + month + "-" + day + " 00:00:00";
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			for(int i = 0; i < pid.length; i++){
				Picture picture = new Picture();
				picture = (Picture) session.get(Picture.class, pid[i]);
				if(picture == null) 
					throw new Exception("图片不存在");
				picture.setPmodpeo(pmodpeo);
				picture.setPdeadline(Timestamp.valueOf(pdeadline));
				session.update(picture);
			}
			session.getTransaction().commit();
			return "admin";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}

	/*----------修改图片确定--------*/
	@RequestMapping("modcheck.do")
	public String ModCheck(@RequestParam(value="pid")String pid, HttpServletRequest request) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Picture picture = (Picture) session.get(Picture.class, pid);
			if(picture == null) 
				throw new Exception("图片不存在");
			Timestamp time = new Timestamp(System.currentTimeMillis()); 
			time.setNanos(0);
			picture.setPchecktime(time);
			picture.setPcheckpeo((String) request.getSession().getAttribute("ID"));
			picture.setPok("YES");
			session.update(picture);
			session.getTransaction().commit();
			return "success";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}

	/*----------拿取检查图片--------*/
	@RequestMapping("modtake.do")
	public String ModTake(@RequestParam(value="pid")String pid, HttpServletRequest request) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Picture picture = (Picture) session.get(Picture.class, pid);
			if(picture == null) 
				throw new Exception("图片不存在");
			picture.setPok("TAKE");
			session.update(picture);
			session.getTransaction().commit();
			return "success";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}

	/*----------查询全部----------*/
	@RequestMapping("selectpicture.do")
	public String SelectPicture(HttpServletRequest request) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "from Picture";  
			Query query = session.createQuery(hql);  
			List<Picture> picturelist = query.list(); 
			String hqll = "from Label label group by label.lname";  
			Query queryl = session.createQuery(hqll);  
			List<Label> labellist = queryl.list(); 
			session.getTransaction().commit();
			request.setAttribute("objlist", picturelist);
			request.setAttribute("labellist", labellist);
			request.setAttribute("cname", "picture");
			return "admin";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}

	/*----------查询全部条件----------*/
	@RequestMapping("selectpictureask.do")
	public String SelectPictureAsk(@RequestParam(value="year")String year, @RequestParam(value="month")String month, @RequestParam(value="day")String day, @RequestParam(value="lname")String lname, HttpServletRequest request) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "select picture.* from Picture picture left join Label label on picture.pid=label.pid where true ";
			if(!lname.equals("0"))
				hql = hql + "and label.lname='" + lname + "' ";
			if(!year.equals("0")){
				String dateend;
				if(month.equals("0")){
					dateend = Integer.valueOf(year)+1 + "-1-1 00:00:00";
				}
				else if(day.equals("0")){
					if(month.equals("12"))
						dateend = Integer.valueOf(year)+1 + "-1-1 00:00:00";
					else
						dateend = year + "-" + Integer.valueOf(month)+1 + "-1 00:00:00";
				}
				else{
					dateend = year + "-" + month + "-" + day + " 01:00:00";
				}
				hql = hql + "and picture.pdeadline<'" + dateend + "' ";
			}
			Query query = session.createSQLQuery(hql).addEntity(Picture.class);  
			List<Picture> picturelist = query.list(); 
			String hqll = "from Label label group by label.lname";  
			Query queryl = session.createQuery(hqll);  
			List<Label> labellist = queryl.list(); 
			session.getTransaction().commit();
			request.setAttribute("objlist", picturelist);
			request.setAttribute("labellist", labellist);
			request.setAttribute("lname", lname);
			request.setAttribute("cname", "picture");
			return "admin";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}

	/*----------查询分配任务----------*/
	@RequestMapping("selectpicturemod.do")
	public String SelectPictureMod(HttpServletRequest request) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "from Picture where pmodpeo = ''";  
			Query query = session.createQuery(hql);  
			List<Picture> picturelist = query.list();
			String hqll = "from Label label group by label.lname";  
			Query queryl = session.createQuery(hqll);  
			List<Label> labellist = queryl.list(); 
			String hqlw = "from Worker where type='mod'";  
			Query queryw = session.createQuery(hqlw);  
			List<Worker> workerlist = queryw.list(); 
			session.getTransaction().commit();
			request.setAttribute("objlist", picturelist);
			request.setAttribute("labellist", labellist);
			request.setAttribute("workerlist", workerlist);
			request.setAttribute("cname", "picturemod");
			return "admin";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}

	/*----------查询分配任务条件----------*/
	@RequestMapping("selectpicturemodask.do")
	public String SelectPictureModAsk(@RequestParam(value="year")String year, @RequestParam(value="month")String month, @RequestParam(value="day")String day, @RequestParam(value="lname")String lname, HttpServletRequest request) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "select picture.* from Picture picture left join Label label on picture.pid=label.pid where picture.pmodpeo = '' ";
			if(!lname.equals("0"))
				hql = hql + "and label.lname='" + lname + "' ";
			if(!year.equals("0")){
				String datestar;
				String dateend;
				if(month.equals("0")){
					datestar = Integer.valueOf(year) + "-1-1 00:00:00";
					dateend = Integer.valueOf(year)+1 + "-1-1 00:00:00";
				}
				else if(day.equals("0")){
					datestar = year + "-" + Integer.valueOf(month) + "-1 00:00:00";
					if(month.equals("12"))
						dateend = Integer.valueOf(year)+1 + "-1-1 00:00:00";
					else
						dateend = year + "-" + Integer.valueOf(month)+1 + "-1 00:00:00";
				}
				else{
					datestar = year + "-" + month + "-" + day + " 00:00:00";
					dateend = year + "-" + month + "-" + day + " 24:00:00";
				}
				hql = hql + "and picture.puptime>='" + datestar + "' " + "and picture.puptime<'" + dateend + "' ";
			}
			Query query = session.createSQLQuery(hql).addEntity(Picture.class); 
			List<Picture> picturelist = query.list();
			String hqll = "from Label label group by label.lname";  
			Query queryl = session.createQuery(hqll);  
			List<Label> labellist = queryl.list(); 
			String hqlw = "from Worker where type='mod'";  
			Query queryw = session.createQuery(hqlw);  
			List<Worker> workerlist = queryw.list(); 
			session.getTransaction().commit();
			request.setAttribute("objlist", picturelist);
			request.setAttribute("labellist", labellist);
			request.setAttribute("workerlist", workerlist);
			request.setAttribute("lname", lname);
			request.setAttribute("cname", "picturemod");
			return "admin";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}

	/*----------查询下一张任务----------*/
	@RequestMapping("selectnextpicture.do")
	public String SelectNextPicture(HttpServletRequest request) {
		try {
			String mod = (String) request.getSession().getAttribute("ID");
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "from Picture where pmodpeo = '"+ mod +"' and pmodtime = '1970-01-02 00:00:00' order by pdeadline,pid";  
			Query query = session.createQuery(hql);  
			List<Picture> picturelist = query.list();
			Picture picture = null;
			if(picturelist.size()!=0)
				picture = picturelist.get(0);
			String hqlok = "select count(picture) from Picture picture where pmodpeo = '"+ mod +"' and pmodtime != '1970-01-02 00:00:00'";  
			Query queryok = session.createQuery(hqlok);  
			int ok = ((Number) queryok.uniqueResult()).intValue(); 
			String hqlsum = "select count(picture) from Picture picture where pmodpeo = '"+ mod +"' ";  
			Query querysum = session.createQuery(hqlsum);  
			int sum = ((Number) querysum.uniqueResult()).intValue(); 
			session.getTransaction().commit();
			request.setAttribute("picture", picture);
			request.setAttribute("ok", ok);
			request.setAttribute("sum", sum);
			return "mod";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}

	/*----------查询下一张任务检查----------*/
	@RequestMapping("selectnextpicturecheck.do")
	public String SelectNextPictureCheck(HttpServletRequest request) {
		try {
			String mod = (String) request.getSession().getAttribute("ID");
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "from Picture where pmodtime != '1970-01-02 00:00:00' and ( pok = '" + mod + "' or pok = '' ) order by pdeadline,pid";  
			Query query = session.createQuery(hql);  
			List<Picture> picturelist = query.list();
			Picture picture = null;
			if(picturelist.size()!=0){
				picture = picturelist.get(0);
				picture.setPok(mod);
				session.update(picture);
			}
			String hqlsum = "select count(picture) from Picture picture where pcheckpeo = '"+ mod +"' ";  
			Query querysum = session.createQuery(hqlsum);  
			int sum = ((Number) querysum.uniqueResult()).intValue(); 
			session.getTransaction().commit();
			request.setAttribute("picture", picture);
			request.setAttribute("sum", sum);
			if(picture != null)
				return "forward:/selecttag.do?pid="+picture.getPid();
			else 
				return "check";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}

	/*----------查询历史----------*/
	@RequestMapping("selecthistory.do")
	public String SelectHistory(HttpServletRequest request) {
		try {
			String id = (String) request.getSession().getAttribute("ID");
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "from Picture where pmodpeo = '"+ id +"' or pcheckpeo = '"+ id +"' ";  
			Query query = session.createQuery(hql);  
			List<Picture> picturelist = query.list();
			/*--------总检查数--------*/
			String hqlcsum = "select count(picture) from Picture picture where pcheckpeo = '"+ id +"' ";  
			Query querycsum = session.createQuery(hqlcsum);  
			int csum = ((Number) querycsum.uniqueResult()).intValue();
			/*--------总标记数--------*/
			String hqlmsum = "select count(picture) from Picture picture where pmodpeo = '"+ id +"' and pmodtime != '1970-01-02 00:00:00' ";  
			Query querymsum = session.createQuery(hqlmsum);  
			int msum = ((Number) querymsum.uniqueResult()).intValue();
			/*--------正确标记数--------*/
			String hqlosum = "select count(picture) from Picture picture where pmodpeo = '"+ id +"' and pok = 'YES' ";  
			Query queryosum = session.createQuery(hqlosum);  
			int osum = ((Number) queryosum.uniqueResult()).intValue();
			/*--------过期标记数--------*/
			String hqldsum = "select count(picture) from Picture picture where pmodpeo = '"+ id +"' and pmodtime > pdeadline ";  
			Query querydsum = session.createQuery(hqldsum);  
			int dsum = ((Number) querydsum.uniqueResult()).intValue();
			/*--------未检查标记数--------*/
			String hqlnsum = "select count(picture) from Picture picture where pmodpeo = '"+ id +"' and pmodtime != '1970-01-02 00:00:00' and pcheckpeo = '' ";  
			Query querynsum = session.createQuery(hqlnsum);  
			int nsum = ((Number) querynsum.uniqueResult()).intValue();
			session.getTransaction().commit();
			request.setAttribute("objlist", picturelist);
			request.setAttribute("csum", csum);
			request.setAttribute("msum", msum);
			request.setAttribute("osum", osum);
			request.setAttribute("dsum", dsum);
			request.setAttribute("nsum", nsum);
			request.setAttribute("id", id);
			return "history";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}

	/*----------管理查询历史----------*/
	@RequestMapping("selecthistoryadmin.do")
	public String SelectHistoryAdmin(@RequestParam(value="id")String id, HttpServletRequest request) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "from Picture where pmodpeo = '"+ id +"' or pcheckpeo = '"+ id +"' ";  
			Query query = session.createQuery(hql);  
			List<Picture> picturelist = query.list();
			/*--------总检查数--------*/
			String hqlcsum = "select count(picture) from Picture picture where pcheckpeo = '"+ id +"' ";  
			Query querycsum = session.createQuery(hqlcsum);  
			int csum = ((Number) querycsum.uniqueResult()).intValue();
			/*--------总标记数--------*/
			String hqlmsum = "select count(picture) from Picture picture where pmodpeo = '"+ id +"' and pmodtime != '1970-01-02 00:00:00' ";  
			Query querymsum = session.createQuery(hqlmsum);  
			int msum = ((Number) querymsum.uniqueResult()).intValue();
			/*--------正确标记数--------*/
			String hqlosum = "select count(picture) from Picture picture where pmodpeo = '"+ id +"' and pok = 'YES' ";  
			Query queryosum = session.createQuery(hqlosum);  
			int osum = ((Number) queryosum.uniqueResult()).intValue();
			/*--------过期标记数--------*/
			String hqldsum = "select count(picture) from Picture picture where pmodpeo = '"+ id +"' and pmodtime > pdeadline ";  
			Query querydsum = session.createQuery(hqldsum);  
			int dsum = ((Number) querydsum.uniqueResult()).intValue();
			/*--------未检查标记数--------*/
			String hqlnsum = "select count(picture) from Picture picture where pmodpeo = '"+ id +"' and pmodtime != '1970-01-02 00:00:00' and pcheckpeo = '' ";  
			Query querynsum = session.createQuery(hqlnsum);  
			int nsum = ((Number) querynsum.uniqueResult()).intValue();
			session.getTransaction().commit();
			request.setAttribute("objlist", picturelist);
			request.setAttribute("csum", csum);
			request.setAttribute("msum", msum);
			request.setAttribute("osum", osum);
			request.setAttribute("dsum", dsum);
			request.setAttribute("nsum", nsum);
			request.setAttribute("id", id);
			return "history";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}
	
	/*----------查询统计----------*/
	@RequestMapping("selectsum.do")
	public String SelectSum(HttpServletRequest request) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hqlsum = "select count(picture) from Picture picture where puptime != '1970-01-02 00:00:00' ";  
			Query querysum = session.createQuery(hqlsum);  
			int sum = ((Number) querysum.uniqueResult()).intValue();
			String hqlmsum = "select count(picture) from Picture picture where pmodtime != '1970-01-02 00:00:00' ";  
			Query querymsum = session.createQuery(hqlmsum);  
			int msum = ((Number) querymsum.uniqueResult()).intValue();
			String hqlcsum = "select count(picture) from Picture picture where pchecktime != '1970-01-02 00:00:00' ";  
			Query querycsum = session.createQuery(hqlcsum);  
			int csum = ((Number) querycsum.uniqueResult()).intValue();
			String hqll = "from Label label group by label.lname";  
			Query queryl = session.createQuery(hqll);  
			List<Label> labellist = queryl.list(); 
			session.getTransaction().commit();
			request.setAttribute("sum", sum);
			request.setAttribute("msum", msum);
			request.setAttribute("csum", csum);
			request.setAttribute("labellist", labellist);
			request.setAttribute("cname", "sum");
			return "admin";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}

	/*----------查询统计条件----------*/
	@RequestMapping("selectsumask.do")
	public String SelectSumAsk(@RequestParam(value="year")String year, @RequestParam(value="month")String month, @RequestParam(value="day")String day, @RequestParam(value="lname")String lname, HttpServletRequest request) {
		try {
			String datestar;
			String dateend;
			if(month.equals("0")){
				datestar = Integer.valueOf(year) + "-1-1 00:00:00";
				dateend = Integer.valueOf(year)+1 + "-1-1 00:00:00";
			}
			else if(day.equals("0")){
				datestar = year + "-" + Integer.valueOf(month) + "-1 00:00:00";
				if(month.equals("12"))
					dateend = Integer.valueOf(year)+1 + "-1-1 00:00:00";
				else
					dateend = year + "-" + Integer.valueOf(month)+1 + "-1 00:00:00";
			}
			else{
				datestar = year + "-" + month + "-" + day + " 00:00:00";
				dateend = year + "-" + month + "-" + day + " 24:00:00";
			}
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			/*--------日期上传查询--------*/
			String hqlsum = "select count(*) from Picture picture left join Label label on picture.pid=label.pid where picture.puptime != '1970-01-02 00:00:00' ";
			if(!lname.equals("0"))
				hqlsum = hqlsum + "and label.lname='" + lname + "' ";
			if(!year.equals("0")){
				hqlsum = hqlsum + "and picture.puptime>='" + datestar + "' " + "and picture.puptime<'" + dateend + "' ";
			}
			Query querysum = session.createSQLQuery(hqlsum); 
			int sum = Integer.parseInt(querysum.list().get(0).toString());
			/*--------日期标记查询--------*/
			String hqlmsum = "select count(*) from Picture picture left join Label label on picture.pid=label.pid where picture.pmodtime != '1970-01-02 00:00:00' ";
			if(!lname.equals("0"))
				hqlmsum = hqlmsum + "and label.lname='" + lname + "' ";
			if(!year.equals("0")){
				hqlmsum = hqlmsum + "and picture.pmodtime>='" + datestar + "' " + "and picture.pmodtime<'" + dateend + "' ";
			}
			Query querymsum = session.createSQLQuery(hqlmsum);  
			int msum = Integer.parseInt(querymsum.list().get(0).toString());
			/*--------日期检查查询--------*/
			String hqlcsum = "select count(*) from Picture picture left join Label label on picture.pid=label.pid where picture.pchecktime != '1970-01-02 00:00:00' ";
			if(!lname.equals("0"))
				hqlcsum = hqlcsum + "and label.lname='" + lname + "' ";
			if(!year.equals("0")){
				hqlcsum = hqlcsum + "and picture.pchecktime>='" + datestar + "' " + "and picture.pchecktime<'" + dateend + "' ";
			}
			Query querycsum = session.createSQLQuery(hqlcsum);  
			int csum = Integer.parseInt(querycsum.list().get(0).toString());
			
			String hqll = "from Label label group by label.lname";  
			Query queryl = session.createQuery(hqll);  
			List<Label> labellist = queryl.list(); 
			session.getTransaction().commit();
			request.setAttribute("sum", sum);
			request.setAttribute("msum", msum);
			request.setAttribute("csum", csum);
			request.setAttribute("labellist", labellist);
			request.setAttribute("cname", "sum");
			return "admin";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}
	
}
