package ma.emsi.benazzouzwalid.tp2webwalidbenazzouz.jsf;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

/**
 * Filtre servlet global chargé de garantir l'encodage UTF-8
 * pour toutes les requêtes et réponses de l'application web.
 */
@WebFilter("/*")
public class CharsetFilter implements Filter {

    /**
     * Intercepte chaque requête HTTP pour imposer l'encodage UTF-8.
     *
     * @param request  la requête entrante
     * @param response la réponse sortante
     * @param chain    la chaîne de filtres suivante
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
             throws IOException, ServletException {

        // S’assurer que le corps des requêtes est lu en UTF-8
        request.setCharacterEncoding("UTF-8");

        // Optionnel : forcer la réponse à utiliser UTF-8
        // ((HttpServletResponse) response).setCharacterEncoding("UTF-8");
        // ((HttpServletResponse) response).setContentType("text/html; charset=UTF-8");

        // Continuer la chaîne de filtres
        chain.doFilter(request, response);
    }

}
