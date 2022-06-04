package service

import (
	"io/ioutil"
	"log"
	"net/http"
	"net/http/cookiejar"
	"net/url"
	"strings"
)

var baseUrl = "https://www.anstoss-online.de/"

type AnstossHttpClient struct {
	user     string
	password string
	http     *http.Client
}

func NewAnstossHttpClient(user string, password string) *AnstossHttpClient {
	jar, _ := cookiejar.New(nil)
	client := &http.Client{
		Jar: jar,
	}
	return &AnstossHttpClient{http: client, user: user, password: password}
}

func (client AnstossHttpClient) Login() {
	data := url.Values{}
	data.Set("login_name", client.user)
	data.Set("login_pw", client.password)
	request, _ := http.NewRequest(http.MethodPost, baseUrl+"content/fixed/login.php", strings.NewReader(data.Encode()))
	request.Header.Add("Content-Type", "application/x-www-form-urlencoded")
	_, err := client.http.Do(request)
	if err != nil {
		log.Fatal(err)
	}
}

func (client AnstossHttpClient) Get(urlPath string) string {
	search := baseUrl + urlPath
	request, _ := http.NewRequest(http.MethodGet, search, nil)
	response, err := client.http.Do(request)
	if err != nil {
		log.Fatal(err)
	}
	responseBody, _ := ioutil.ReadAll(response.Body)
	return string(responseBody)
}
