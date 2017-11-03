package com.itechart.app.controller.utils;

import com.itechart.app.model.dao.ContactDao;
import com.itechart.app.model.dao.JdbcContactDao;
import com.itechart.app.model.utils.JsonConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

public class NationalitySearcherHelper {

    private static final String NATIONALITY_PATTERN_PARAM = "pattern";

    public void searchNationalities(HttpServletRequest request, HttpServletResponse response)
            throws Exception{
        response.setContentType("application/json");
        String searchPattern = request.getParameter(NATIONALITY_PATTERN_PARAM);
        if(searchPattern != null){
            ContactDao dao = JdbcContactDao.newInstance();

            List<String> foundNationalities = dao.findNationalities(searchPattern);
            JsonConverter converter = new JsonConverter();
            String foundNationalitiesJson = converter.toJson(foundNationalities);

            dao.closeDao();
            PrintWriter out = response.getWriter();
            out.print(foundNationalitiesJson);
            out.flush();
        }
    }
}
