package service

import (
	"io"
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

func NewAnstossHttpClient(user string, password string) (*AnstossHttpClient, error) {
	jar, err := cookiejar.New(nil)
	if err != nil {
		return nil, err
	}
	client := &http.Client{
		Jar: jar,
	}
	return &AnstossHttpClient{http: client, user: user, password: password}, nil
}

func (client *AnstossHttpClient) Login() error {
	data := url.Values{}
	data.Set("login_name", client.user)
	data.Set("login_pw", client.password)
	request, err := http.NewRequest(http.MethodPost, baseUrl+"content/fixed/login.php", strings.NewReader(data.Encode()))
	if err != nil {
		return err
	}
	request.Header.Add("Content-Type", "application/x-www-form-urlencoded")
	_, err = client.http.Do(request)
	if err != nil {
		return err
	}
	return nil
}

func (client *AnstossHttpClient) Get(urlPath string) (string, error) {
	search := baseUrl + urlPath
	request, err := http.NewRequest(http.MethodGet, search, nil)
	if err != nil {
		return "", err
	}
	response, err := client.http.Do(request)
	if err != nil {
		return "", err
	}
	responseBody, err := io.ReadAll(response.Body)
	if err != nil {
		return "", err
	}
	return string(responseBody), nil
}

func (client *AnstossHttpClient) Post(urlPath string) (string, error) {
	search := baseUrl + urlPath
	request, err := http.NewRequest(http.MethodPost, search, nil)
	if err != nil {
		return "", err
	}
	response, err := client.http.Do(request)
	if err != nil {
		return "", err
	}
	responseBody, err := io.ReadAll(response.Body)
	if err != nil {
		return "", err
	}
	return string(responseBody), nil
}
