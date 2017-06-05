@extends('layouts.app')

@section('content')
<div class="container">
    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <div class="panel panel-default">
                <div class="panel-heading">Send Notification</div>

                <div class="panel-body">
                    @if (count($errors) > 0)
                        <div class="alert alert-danger">
                            <ul>
                                @foreach ($errors->all() as $error)
                                    <li>{{ $error }}</li>
                                @endforeach
                            </ul>
                        </div>
                    @endif

                    @if (session('success'))
                        <div class="alert alert-success">
                            {{ session('success') }}
                        </div>
                    @endif

                    <form action="/notification/send" method="POST">
                        {{ csrf_field() }}
                        <div class="form-group">
                            <label for="exampleInputEmail1">Receivers first name</label>
                            <input type="text" class="form-control" id="receiverFirstNameInput" name="receiver_first_name" placeholder="Enter receivers first name">
                        </div>
                        <div class="form-group">
                            <label for="exampleInputEmail1">Receivers last name</label>
                            <input type="text" class="form-control" id="receiverLastName" name="receiver_last_name" placeholder="Enter receivers last name">
                        </div>
                        <div class="form-group">
                            <label for="exampleInputPassword1">Title</label>
                            <input type="text" class="form-control" id="notificationTitleInput" name="notification_title" placeholder="Enter title">
                        </div>
                        <div class="form-group">
                            <label for="exampleInputPassword1">Body</label>
                            <input type="text" class="form-control" id="notificationBodyInput" name="notification_body" placeholder="Enter text">
                        </div>
                        <button type="submit" class="btn btn-primary">Send Notification</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
@endsection
